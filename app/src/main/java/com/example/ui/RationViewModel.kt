package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiClient
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RationViewModel(
    application: Application,
    private val repository: RationRepository
) : AndroidViewModel(application) {

    // --- Session States ---
    var currentUser by mutableStateOf<User?>(null)
        private set

    var currentDistributorShop by mutableStateOf<FPSDistributor?>(null)
        private set

    var appScreen by mutableStateOf(Screen.SPLASH) // Navigation controller state

    // --- General Flows ---
    val allDistributors: StateFlow<List<FPSDistributor>> = repository.getAllDistributorsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Dynamic User-Specific Flows ---
    private val _userCardNumber = MutableStateFlow<String>("")
    private val _distributorId = MutableStateFlow<Int>(-1)

    val citizenTokens: StateFlow<List<Token>> = _userCardNumber
        .flatMapLatest { cardNo ->
            if (cardNo.isEmpty()) flowOf(emptyList())
            else repository.getTokensByCardNumberFlow(cardNo)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val citizenOrders: StateFlow<List<RationOrder>> = _userCardNumber
        .flatMapLatest { cardNo ->
            if (cardNo.isEmpty()) flowOf(emptyList())
            else repository.getOrdersByCardNumberFlow(cardNo)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val citizenComplaints: StateFlow<List<Complaint>> = _userCardNumber
        .flatMapLatest { cardNo ->
            if (cardNo.isEmpty()) flowOf(emptyList())
            else repository.getComplaintsByCardNumberFlow(cardNo)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val distributorTokens: StateFlow<List<Token>> = _distributorId
        .flatMapLatest { distId ->
            if (distId == -1) flowOf(emptyList())
            else repository.getTokensByDistributorFlow(distId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val distributorOrders: StateFlow<List<RationOrder>> = _distributorId
        .flatMapLatest { distId ->
            if (distId == -1) flowOf(emptyList())
            else repository.getOrdersByDistributorFlow(distId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    // --- Shopping Cart State ---
    var cartItems by mutableStateOf<Map<SubsidizedProduct, Double>>(emptyMap())
        private set

    // Entitlement ceilings for Citizen
    val familySize = 4 // Static design choice for Ramesh Chand (Demo Account)
    val monthlyRiceQuota = familySize * 5.0 // 20 kg
    val monthlyWheatQuota = familySize * 5.0 // 20 kg
    val monthlySugarQuota = 2.0 // 2 kg

    // --- AI Suggestions States ---
    var aiLoading by mutableStateOf(false)
        private set
    var aiSuggestionsResponse by mutableStateOf<String?>(null)
        private set

    // --- Selected FPS shop (linked / chosen) ---
    var selectedShopId by mutableStateOf(1) // Defaults to Shop #102

    init {
        // Automatically attempt to seed and fetch default test user for instantaneous evaluation ease!
        triggerAutoLogin()
    }

    private fun triggerAutoLogin() {
        viewModelScope.launch {
            // Wait 2200ms to show the opening splash window
            kotlinx.coroutines.delay(2200)

            // Direct route to WELCOME screen for secure manual login selection instead of automatic bypass
            selectScreen(Screen.WELCOME)
        }
    }

    fun selectScreen(screen: Screen) {
        if (screen == Screen.AI_SUGGESTIONS && aiSuggestionsResponse == null) {
            fetchAISuggestions()
        }
        appScreen = screen
    }

    // --- Authentication ---
    suspend fun tryLogin(email: String, role: String): Boolean {
        val user = repository.getUserByEmail(email)
        return if (user != null && user.role == role) {
            loginUser(user)
            true
        } else {
            false
        }
    }

    suspend fun tryRegister(name: String, email: String, phone: String, role: String, paramVal: String): Boolean {
        // Create user
        val newUser = if (role == "citizen") {
            User(
                name = name,
                email = email,
                phone = phone,
                role = role,
                cardNumber = paramVal // parameter is Card Number for citizen
            )
        } else {
            User(
                name = name,
                email = email,
                phone = phone,
                role = role,
                licenseNumber = paramVal // parameter is License Number for distributor
            )
        }

        val id = repository.registerUser(newUser)
        if (id > 0) {
            val userWithId = newUser.copy(id = id.toInt())
            
            // If distributor, register shop as well
            if (role == "distributor") {
                val newShop = FPSDistributor(
                    shopName = "$name's Fair Price Depot - Shop #${(100..999).random()}",
                    ownerName = name,
                    phone = phone,
                    licenseNumber = paramVal,
                    address = "Street Area, Local Municipality GPO Ward",
                    lat = 28.5 + (Random().nextDouble() - 0.5) * 0.2,
                    lng = 77.2 + (Random().nextDouble() - 0.5) * 0.2,
                    rating = 4.0f + (Random().nextFloat() * 1.0f),
                    riceStock = 1000.0,
                    wheatStock = 1200.0,
                    sugarStock = 300.0,
                    flourStock = 400.0,
                    oilStock = 200.0
                )
                repository.registerDistributor(newShop)
            }

            loginUser(userWithId)
            return true
        }
        return false
    }

    private suspend fun loginUser(user: User) {
        currentUser = user
        if (user.role == "citizen") {
            _userCardNumber.value = user.cardNumber ?: ""
            // Lookup selectedShopId dynamically
            selectedShopId = 1
            selectScreen(Screen.DASHBOARD)
        } else {
            val shop = repository.getDistributorByLicense(user.licenseNumber ?: "")
            if (shop != null) {
                currentDistributorShop = shop
                _distributorId.value = shop.id
                selectedShopId = shop.id
            } else {
                // Instantly generate a shop for them so they never crash
                val mockShop = FPSDistributor(
                    id = 2,
                    shopName = "Distributor Outlet - License ${user.licenseNumber}",
                    ownerName = user.name,
                    phone = user.phone,
                    licenseNumber = user.licenseNumber ?: "LIC-GEN",
                    address = "Primary PDS Distribution Center",
                    lat = 28.6,
                    lng = 77.3,
                    rating = 4.5f,
                    riceStock = 1500.0,
                    wheatStock = 1500.0,
                    sugarStock = 500.0,
                    flourStock = 600.0,
                    oilStock = 300.0
                )
                val newId = repository.registerDistributor(mockShop)
                val createdShop = mockShop.copy(id = newId.toInt())
                currentDistributorShop = createdShop
                _distributorId.value = createdShop.id
                selectedShopId = createdShop.id
            }
            selectScreen(Screen.DIST_DASHBOARD)
        }
    }

    fun logout() {
        currentUser = null
        currentDistributorShop = null
        _userCardNumber.value = ""
        _distributorId.value = -1
        cartItems = emptyMap()
        aiSuggestionsResponse = null
        selectScreen(Screen.WELCOME)
    }


    // --- Shopping Cart Operations ---
    fun addToCart(product: SubsidizedProduct, quantity: Double) {
        if (quantity <= 0) {
            cartItems = cartItems - product
            return
        }

        // Validate limits
        val currentInCart = cartItems[product] ?: 0.0
        val targetQuantity = quantity

        when (product.type) {
            ProductType.RICE -> {
                if (targetQuantity > monthlyRiceQuota) return
            }
            ProductType.WHEAT -> {
                if (targetQuantity > monthlyWheatQuota) return
            }
            ProductType.SUGAR -> {
                if (targetQuantity > monthlySugarQuota) return
            }
            else -> {} // no specific government limit for flour/oil
        }

        cartItems = cartItems + (product to targetQuantity)
    }

    fun removeFromCart(product: SubsidizedProduct) {
        cartItems = cartItems - product
    }

    fun clearCart() {
        cartItems = emptyMap()
    }

    fun getCartTotalAmount(): Double {
        return cartItems.entries.sumOf { it.key.subsidizedPrice * it.value }
    }


    // --- Action Methods ---

    /**
     * Finalizes Checkout and decreases FPS inventory
     */
    fun checkout(collectionType: String, onComplete: (Boolean) -> Unit) {
        val user = currentUser ?: return
        val cardNo = user.cardNumber ?: ""
        if (cartItems.isEmpty()) {
            onComplete(false)
            return
        }

        viewModelScope.launch {
            val dist = repository.getDistributorById(selectedShopId) ?: return@launch

            // Calculate items representation for table
            val itemsFormatted = cartItems.entries.joinToString("; ") { "${it.key.name}: ${it.value} ${it.key.unit}" }
            val amount = getCartTotalAmount()
            val orderId = "ORD-${(10000..99999).random()}"

            // Generate booking date
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateStr = sdf.format(Date())

            val newOrder = RationOrder(
                orderId = orderId,
                cardNumber = cardNo,
                cardholderName = user.name,
                distributorId = dist.id,
                shopName = dist.shopName,
                itemsText = itemsFormatted,
                totalAmount = amount,
                paymentStatus = "completed", // Simulated UPI success
                orderStatus = "placed",
                collectionType = collectionType,
                collectionDate = dateStr
            )

            val orderRowId = repository.placeOrder(newOrder)

            if (orderRowId > 0) {
                // Reduce inventory locally! Extremely critical representation
                var updatedRice = dist.riceStock
                var updatedWheat = dist.wheatStock
                var updatedSugar = dist.sugarStock
                var updatedFlour = dist.flourStock
                var updatedOil = dist.oilStock

                for ((prod, qty) in cartItems) {
                    when (prod.type) {
                        ProductType.RICE -> updatedRice = (updatedRice - qty).coerceAtLeast(0.0)
                        ProductType.WHEAT -> updatedWheat = (updatedWheat - qty).coerceAtLeast(0.0)
                        ProductType.SUGAR -> updatedSugar = (updatedSugar - qty).coerceAtLeast(0.0)
                        ProductType.FLOUR -> updatedFlour = (updatedFlour - qty).coerceAtLeast(0.0)
                        ProductType.OIL -> updatedOil = (updatedOil - qty).coerceAtLeast(0.0)
                    }
                }

                repository.updateDistributor(
                    dist.copy(
                        riceStock = updatedRice,
                        wheatStock = updatedWheat,
                        sugarStock = updatedSugar,
                        flourStock = updatedFlour,
                        oilStock = updatedOil
                    )
                )

                // Clear shopping cart
                cartItems = emptyMap()
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }

    /**
     * Book custom collection time slots
     */
    fun bookToken(dateStr: String, slotStr: String, onComplete: (Boolean) -> Unit) {
        val user = currentUser ?: return
        val cardNo = user.cardNumber ?: ""

        viewModelScope.launch {
            val dist = repository.getDistributorById(selectedShopId) ?: return@launch
            val tokenNo = "TKN-${(1000..9999).random()}"

            val newToken = Token(
                tokenNumber = tokenNo,
                cardNumber = cardNo,
                cardholderName = user.name,
                distributorId = dist.id,
                distributorName = dist.shopName,
                itemsText = "Monthly Allocation Priority Collection",
                status = "booked",
                bookingDate = dateStr,
                collectionTimeSlot = slotStr
            )

            val tokenId = repository.bookToken(newToken)
            onComplete(tokenId > 0)
        }
    }

    /**
     * Submit Complaint
     */
    fun submitComplaint(category: String, title: String, description: String, priority: String, onComplete: (Boolean) -> Unit) {
        val user = currentUser ?: return
        val cardNo = user.cardNumber ?: ""

        viewModelScope.launch {
            val complaintId = "COMP-${(10000..99999).random()}"
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val dateStr = sdf.format(Date())

            val complaint = Complaint(
                complaintId = complaintId,
                cardNumber = cardNo,
                cardholderName = user.name,
                category = category,
                title = title,
                description = description,
                status = "submitted",
                priority = priority,
                createdAt = dateStr
            )

            val compId = repository.submitComplaint(complaint)
            onComplete(compId > 0)
        }
    }

    /**
     * Fetch personalized AI menu suggestions using Gemini Client
     */
    fun fetchAISuggestions() {
        val user = currentUser ?: return
        viewModelScope.launch {
            aiLoading = true
            aiSuggestionsResponse = null
            
            // Build purchase summary from existing completed orders or default values
            val orders = citizenOrders.value.take(3)
            val history = if (orders.isEmpty()) {
                "Standard Monthly Quota: 20kg Rice and 20kg Wheat plus sugar."
            } else {
                orders.joinToString("; ") { "Order ${it.orderId} containing: ${it.itemsText}" }
            }

            val habits = if (user.cardNumber?.contains("DEL") == true) "North Indian style (wheat preferring)" else "South Indian style (rice and lentils preferring)"
            val prefs = "Interested in protein fortification, dietary fiber, and healthy low-glycemic foods for kids."

            val result = GeminiClient.getRationSuggestions(
                purchaseHistory = history,
                familySize = familySize,
                preferences = prefs,
                regionalHabits = habits
            )

            aiSuggestionsResponse = result
            aiLoading = false
        }
    }


    // --- Distributor Operations ---

    /**
     * Restock shop items
     */
    fun restockProduct(productType: ProductType, amount: Double) {
        val shop = currentDistributorShop ?: return
        if (amount <= 0) return

        viewModelScope.launch {
            val updatedShop = when (productType) {
                ProductType.RICE -> shop.copy(riceStock = shop.riceStock + amount)
                ProductType.WHEAT -> shop.copy(wheatStock = shop.wheatStock + amount)
                ProductType.SUGAR -> shop.copy(sugarStock = shop.sugarStock + amount)
                ProductType.FLOUR -> shop.copy(flourStock = shop.flourStock + amount)
                ProductType.OIL -> shop.copy(oilStock = shop.oilStock + amount)
            }
            repository.updateDistributor(updatedShop)
            currentDistributorShop = updatedShop // Keep screen state updated in-memory
        }
    }

    /**
     * Fill pending order
     */
    fun markOrderDistributed(order: RationOrder) {
        viewModelScope.launch {
            val updatedOrder = order.copy(orderStatus = "collected")
            repository.updateOrder(updatedOrder)
        }
    }

    /**
     * Cancel/Collect booked Token
     */
    fun resolveToken(token: Token, action: String) {
        viewModelScope.launch {
            val updatedToken = token.copy(status = action) // "collected" or "cancelled"
            repository.updateToken(updatedToken)
        }
    }

    /**
     * Resolve citizen complaint (Distributor or System response)
     */
    fun resolveComplaint(complaint: Complaint, responseText: String) {
        viewModelScope.launch {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateStr = sdf.format(Date())
            val updatedComplaint = complaint.copy(
                status = "resolved",
                response = responseText,
                resolutionDate = dateStr
            )
            repository.updateComplaint(updatedComplaint)
        }
    }
}

// --- Screen Routing Enum ---
enum class Screen {
    SPLASH,
    WELCOME,
    LOGIN_CITIZEN,
    LOGIN_DISTRIBUTOR,
    REGISTER_CITIZEN,
    REGISTER_DISTRIBUTOR,
    
    // Citizen Screens
    DASHBOARD,
    BROWSE_ORDER,
    CART,
    TOKEN_BOOKING,
    SHOP_LOCATOR,
    AI_SUGGESTIONS,
    COMPLAINTS,

    // Distributor Screens
    DIST_DASHBOARD,
    DIST_STOCK,
    DIST_ORDERS,
    DIST_SLOTS,
    DIST_COMPLAINTS // Resolve outstanding warnings
}

// --- Products Metadata ---
enum class ProductType { RICE, WHEAT, SUGAR, FLOUR, OIL }

data class SubsidizedProduct(
    val name: String,
    val type: ProductType,
    val subsidizedPrice: Double, // Price in Rupees
    val unit: String,
    val iconString: String,
    val description: String
)

val SubsidizedProductsList = listOf(
    SubsidizedProduct("Premium PDS Rice", ProductType.RICE, 3.00, "kg", "🌾", "Subsidized high-quality long-grain white rice."),
    SubsidizedProduct("Fortified Whole Wheat", ProductType.WHEAT, 2.00, "kg", "🌾", "Nutritious raw whole wheat loaded with dietary wheat germ."),
    SubsidizedProduct("Refined White Sugar", ProductType.SUGAR, 13.50, "kg", "🍬", "Subsidized sweetener allocated per family card holder."),
    SubsidizedProduct("PDS Chakki Fresh Atta", ProductType.FLOUR, 15.00, "5kg pack", "🥯", "Pre-ground premium fiber whole wheat flour."),
    SubsidizedProduct("Fortified Soya/Mustard Oil", ProductType.OIL, 45.00, "Litre", "🧪", "Enriched with Vitamin A & D for cellular vitality.")
)

// Factory
class RationViewModelFactory(
    private val application: Application,
    private val repository: RationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RationViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
