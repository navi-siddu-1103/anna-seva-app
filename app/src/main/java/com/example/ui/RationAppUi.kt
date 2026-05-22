package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RationAppUi(viewModel: RationViewModel) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            if (viewModel.appScreen != Screen.SPLASH
                && viewModel.appScreen != Screen.WELCOME 
                && viewModel.appScreen != Screen.LOGIN_CITIZEN 
                && viewModel.appScreen != Screen.LOGIN_DISTRIBUTOR
                && viewModel.appScreen != Screen.REGISTER_CITIZEN 
                && viewModel.appScreen != Screen.REGISTER_DISTRIBUTOR) {
                AppHeader(viewModel)
            }
        },
        bottomBar = {
            if (viewModel.currentUser != null && viewModel.appScreen != Screen.SPLASH) {
                AppBottomBar(viewModel)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = viewModel.appScreen,
                transitionSpec = {
                    fadeIn(animationSpec = spring()) togetherWith fadeOut(animationSpec = spring())
                },
                label = "ScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.SPLASH -> SplashScreen(viewModel)
                    Screen.WELCOME -> WelcomeScreen(viewModel)
                    Screen.LOGIN_CITIZEN -> LoginScreen(viewModel, role = "citizen")
                    Screen.LOGIN_DISTRIBUTOR -> LoginScreen(viewModel, role = "distributor")
                    Screen.REGISTER_CITIZEN -> RegisterScreen(viewModel, role = "citizen")
                    Screen.REGISTER_DISTRIBUTOR -> RegisterScreen(viewModel, role = "distributor")
                    
                    // Citizen App
                    Screen.DASHBOARD -> CitizenDashboard(viewModel)
                    Screen.BROWSE_ORDER -> BrowseOrderScreen(viewModel)
                    Screen.CART -> CartScreen(viewModel)
                    Screen.TOKEN_BOOKING -> TokenBookingScreen(viewModel)
                    Screen.SHOP_LOCATOR -> ShopLocatorScreen(viewModel)
                    Screen.AI_SUGGESTIONS -> AISuggestionsScreen(viewModel)
                    Screen.COMPLAINTS -> ComplaintsScreen(viewModel)

                    // Distributor App
                    Screen.DIST_DASHBOARD -> DistributorDashboard(viewModel)
                    Screen.DIST_STOCK -> DistributorStockScreen(viewModel)
                    Screen.DIST_ORDERS -> DistributorOrdersScreen(viewModel)
                    Screen.DIST_SLOTS -> DistributorSlotsScreen(viewModel)
                    Screen.DIST_COMPLAINTS -> DistributorComplaintsScreen(viewModel)
                }
            }
        }
    }
}

// ==========================================
// SCREEN 0: Opening Splash Screen (Natural Tones)
// ==========================================
@Composable
fun SplashScreen(viewModel: RationViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PaleWarmBackground, // Ivory white background
                        WarmSurface        // Soft grey-green surface
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Elegant background pattern using custom brush style or soft circles
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(SoftSageContainer, CircleShape)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(PrimaryForestGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🍚",
                        fontSize = 48.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Anna Seva",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Serif,
                color = PrimaryForestGreen,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("splash_app_title")
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Digital e-Ration App",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = SoftSageDarkText,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Nourishing Lives, Connecting Communities",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = SoftSageDarkText.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Elegant Natural Tone Loader
            CircularProgressIndicator(
                color = PrimaryForestGreen,
                trackColor = SoftSageContainer,
                strokeWidth = 4.dp,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))
        }

        // Footer at the bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = SoftSageContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, NeutralBorder.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Secure lock",
                        tint = PrimaryForestGreen,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Secure Digital PDS Initiative",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryForestGreen
                    )
                }
            }
        }
    }
}

// --- Header View ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(viewModel: RationViewModel) {
    val user = viewModel.currentUser
    val isCitizen = user?.role == "citizen"

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🍚 Anna Seva",
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                )
                Badge(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(text = if (isCitizen) "Citizen" else "Depot Owner", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        actions = {
            IconButton(
                onClick = { viewModel.logout() },
                modifier = Modifier.testTag("logout_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Log Out",
                    tint = ErrorRed
                )
            }
        }
    )
}

// --- Dynamic Navigation Bar ---
@Composable
fun AppBottomBar(viewModel: RationViewModel) {
    val user = viewModel.currentUser ?: return
    val currentScreen = viewModel.appScreen

    if (user.role == "citizen") {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.shadow(8.dp)
        ) {
            NavigationBarItem(
                selected = currentScreen == Screen.DASHBOARD,
                onClick = { viewModel.selectScreen(Screen.DASHBOARD) },
                icon = { Icon(Icons.Default.Home, null) },
                label = { Text("Home", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.BROWSE_ORDER,
                onClick = { viewModel.selectScreen(Screen.BROWSE_ORDER) },
                icon = { 
                    BadgedBox(
                        badge = {
                            if (viewModel.cartItems.isNotEmpty()) {
                                Badge(containerColor = AccentSpicyOrange, contentColor = Color.White) {
                                    Text(viewModel.cartItems.size.toString())
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.ShoppingCart, null)
                    }
                },
                label = { Text("Order", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.TOKEN_BOOKING,
                onClick = { viewModel.selectScreen(Screen.TOKEN_BOOKING) },
                icon = { Icon(Icons.Default.DateRange, null) },
                label = { Text("Token", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.SHOP_LOCATOR,
                onClick = { viewModel.selectScreen(Screen.SHOP_LOCATOR) },
                icon = { Icon(Icons.Default.LocationOn, null) },
                label = { Text("Map", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.AI_SUGGESTIONS,
                onClick = { viewModel.selectScreen(Screen.AI_SUGGESTIONS) },
                icon = { Icon(Icons.Default.Star, null) },
                label = { Text("AI Tips", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            )
        }
    } else {
        // Distributor Navigation Bar (Using Core Icons)
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.shadow(8.dp)
        ) {
            NavigationBarItem(
                selected = currentScreen == Screen.DIST_DASHBOARD,
                onClick = { viewModel.selectScreen(Screen.DIST_DASHBOARD) },
                icon = { Icon(Icons.Default.Info, null) },
                label = { Text("Metrics") }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.DIST_STOCK,
                onClick = { viewModel.selectScreen(Screen.DIST_STOCK) },
                icon = { Icon(Icons.Default.List, null) },
                label = { Text("Stock") }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.DIST_ORDERS,
                onClick = { viewModel.selectScreen(Screen.DIST_ORDERS) },
                icon = { Icon(Icons.Default.Build, null) },
                label = { Text("Fulfill") }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.DIST_SLOTS,
                onClick = { viewModel.selectScreen(Screen.DIST_SLOTS) },
                icon = { Icon(Icons.Default.AccountBox, null) },
                label = { Text("Slots") }
            )
            NavigationBarItem(
                selected = currentScreen == Screen.DIST_COMPLAINTS,
                onClick = { viewModel.selectScreen(Screen.DIST_COMPLAINTS) },
                icon = { Icon(Icons.Default.Warning, null) },
                label = { Text("Alerts") }
            )
        }
    }
}


// ==========================================
// SCREEN 1: Welcome / Role Selection
// ==========================================
@Composable
fun WelcomeScreen(viewModel: RationViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                Brush.verticalGradient(
                    colors = listOf(PaleWarmBackground, WarmSurface)
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(PrimaryGoldenYellow, CircleShape)
                .padding(16.dp)
                .shadow(12.dp, CircleShape, clip = true),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🍚",
                fontSize = 64.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Anna Seva App",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Serif,
            color = AccentSpicyOrange,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Digital e-Ration Suvidha",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkGrayText.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Bringing ultimate transparency, zero queues, and digital security to the Public Distribution System (PDS).",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = DarkGrayText.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { viewModel.selectScreen(Screen.LOGIN_CITIZEN) }
                .testTag("citizen_portal_btn"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(PaleWarmBackground, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = AccentSpicyOrange,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ration Card Holder",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrayText
                    )
                    Text(
                        text = "Order rations monthly, book token slots, and get healthy nutritional suggestions.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = AccentSpicyOrange)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
                .clickable { viewModel.selectScreen(Screen.LOGIN_DISTRIBUTOR) }
                .testTag("distributor_portal_btn"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(PaleWarmBackground, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = SecondarySaffron,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Fair Price Shop Owner",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrayText
                    )
                    Text(
                        text = "Manage inventory stock levels, review bookings, and distribute digital orders.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = SecondarySaffron)
            }
        }
    }
}


// ==========================================
// SCREEN 2: Login Screens
// ==========================================
@Composable
fun LoginScreen(viewModel: RationViewModel, role: String) {
    var email by remember { mutableStateOf("ramesh@example.com") }
    var pinOtp by remember { mutableStateOf("123456") }
    var otpSent by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(role) {
        if (role == "citizen") {
            email = "ramesh@example.com"
        } else {
            email = "rajesh@example.com"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            TextButton(onClick = { viewModel.selectScreen(Screen.WELCOME) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                Text(" Back Home", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (role == "citizen") "Citizen Login" else "Depot Owner Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Serif,
            color = DarkGrayText
        )
        Text(
            text = if (role == "citizen") "Verify with registered Card Email" else "Verify with License Owner credentials",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Registered Email Address", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("e.g. ramesh@example.com") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 16.dp)
                        .testTag("login_email_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                if (otpSent) {
                    Text("Enter 6-Digit OTP Sent via SMS/Email", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                    OutlinedTextField(
                        value = pinOtp,
                        onValueChange = { pinOtp = it },
                        placeholder = { Text("123456") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 16.dp)
                            .testTag("login_otp_input"),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                errorMsg?.let {
                    Text(
                        text = it,
                        color = ErrorRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Button(
                    onClick = {
                        errorMsg = null
                        if (!otpSent) {
                            if (email.isNotBlank()) {
                                otpSent = true
                            } else {
                                errorMsg = "Please enter your registered email address."
                            }
                        } else {
                            scope.launch {
                                val loginSuccess = viewModel.tryLogin(email, role)
                                if (!loginSuccess) {
                                    errorMsg = "Account not found with this email for selected role. Please register."
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("login_submit_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGoldenYellow, contentColor = Color.Black)
                ) {
                    Text(
                        text = if (!otpSent) "Request SMS/Email OTP 📲" else "Verify OTP & Access App 🔑",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an digital account? ", fontSize = 13.sp, color = Color.Gray)
            Text(
                text = "Register Now",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AccentSpicyOrange,
                modifier = Modifier
                    .clickable { 
                        if (role == "citizen") viewModel.selectScreen(Screen.REGISTER_CITIZEN) 
                        else viewModel.selectScreen(Screen.REGISTER_DISTRIBUTOR)
                    }
                    .testTag("goto_register_cta")
            )
        }
    }
}


// ==========================================
// SCREEN 3: Register Screens
// ==========================================
@Composable
fun RegisterScreen(viewModel: RationViewModel, role: String) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var paramField by remember { mutableStateOf("") } 
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            TextButton(onClick = { viewModel.selectScreen(Screen.WELCOME) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                Text(" Back Home", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (role == "citizen") "App Registration" else "Dealer Registration",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Serif,
            color = DarkGrayText
        )
        Text(
            text = "Create a secure digital identity linked to physical ration database.",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Full Name", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("e.g. Ramesh Chand") },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 12.dp).testTag("reg_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Text("Email Address", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("ramesh@example.com") },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 12.dp).testTag("reg_email_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Text("Mobile Number", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("9876543210") },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 12.dp).testTag("reg_phone_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Text(
                    text = if (role == "citizen") "Ration Card Number" else "FPS Depot License Number",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrayText
                )
                OutlinedTextField(
                    value = paramField,
                    onValueChange = { paramField = it },
                    placeholder = { Text(if (role == "citizen") "RC-DEL-104928A" else "FPS-2015-88432") },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp).testTag("reg_param_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                errorMsg?.let {
                    Text(
                        text = it,
                        color = ErrorRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Button(
                    onClick = {
                        errorMsg = null
                        if (name.isBlank() || email.isBlank() || phone.isBlank() || paramField.isBlank()) {
                            errorMsg = "Please fill in all details."
                        } else {
                            scope.launch {
                                val success = viewModel.tryRegister(name, email, phone, role, paramField)
                                if (!success) {
                                    errorMsg = "Registration failed. Username/Card number may already exist."
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("reg_submit_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentSpicyOrange, contentColor = Color.White)
                ) {
                    Text("Securely Register & Enter App 🛡️", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}


// ==========================================
// ROLE 1: CITIZEN SCREENS
// ==========================================

// --- CITIZEN DASHBOARD ---
@Composable
fun CitizenDashboard(viewModel: RationViewModel) {
    val user = viewModel.currentUser ?: return
    val tokens by viewModel.citizenTokens.collectAsState(emptyList())
    val orders by viewModel.citizenOrders.collectAsState(emptyList())
    val distributors by viewModel.allDistributors.collectAsState(emptyList())

    val linkedShop = distributors.find { it.id == viewModel.selectedShopId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Welcome Back,",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = "${user.name} 👋",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star, 
                        contentDescription = null, 
                        modifier = Modifier.size(16.dp), 
                        tint = AccentSpicyOrange
                    )
                    Text(
                        text = " Ration Card ID: ${user.cardNumber}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Linked Shop: ${linkedShop?.shopName ?: "Checking details..."}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }

        Text(
            text = "Your Subsidized Entitlements (Monthly)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuotaProgressGauge(
                label = "Rice",
                allocatedQty = viewModel.monthlyRiceQuota,
                purchasedQty = calculatePurchasedQuantity(orders, ProductType.RICE),
                unit = "Kg",
                color = PrimaryGoldenYellow,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            QuotaProgressGauge(
                label = "Wheat",
                allocatedQty = viewModel.monthlyWheatQuota,
                purchasedQty = calculatePurchasedQuantity(orders, ProductType.WHEAT),
                unit = "Kg",
                color = SecondarySaffron,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            QuotaProgressGauge(
                label = "Sugar",
                allocatedQty = viewModel.monthlySugarQuota,
                purchasedQty = calculatePurchasedQuantity(orders, ProductType.SUGAR),
                unit = "Kg",
                color = AccentSpicyOrange,
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "Easy Access Quick Menus",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickMenuCard(
                label = "E-Ration Checkout",
                desc = "Subsidized items available",
                icon = Icons.Default.ShoppingCart,
                bgColor = PrimaryGoldenYellow.copy(alpha = 0.15f),
                onClick = { viewModel.selectScreen(Screen.BROWSE_ORDER) },
                modifier = Modifier.weight(1f)
            )
            QuickMenuCard(
                label = "Book Time Slot",
                desc = "Reduce standing lines",
                icon = Icons.Default.DateRange,
                bgColor = AccentSpicyOrange.copy(alpha = 0.15f),
                onClick = { viewModel.selectScreen(Screen.TOKEN_BOOKING) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickMenuCard(
                label = "Find FPS Depot",
                desc = "See live stocks & location",
                icon = Icons.Default.LocationOn,
                bgColor = InfoBlue.copy(alpha = 0.15f),
                onClick = { viewModel.selectScreen(Screen.SHOP_LOCATOR) },
                modifier = Modifier.weight(1f)
            )
            QuickMenuCard(
                label = "Submit Complaint",
                desc = "Report issues or concerns",
                icon = Icons.Default.Warning,
                bgColor = SuccessGreen.copy(alpha = 0.15f),
                onClick = { viewModel.selectScreen(Screen.COMPLAINTS) },
                modifier = Modifier.weight(1f)
            )
        }

        val activeToken = tokens.find { it.status == "booked" }
        if (activeToken != null) {
            Text(
                text = "Upcoming Token Appointment",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGrayText,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.1f)),
                border = BorderStroke(1.dp, SuccessGreen)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(SuccessGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎟️", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Token ID: ${activeToken.tokenNumber}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                        Text(
                            text = "Appointment: ${activeToken.bookingDate} (${activeToken.collectionTimeSlot})",
                            fontSize = 13.sp,
                            color = DarkGrayText
                        )
                        Text(
                            text = "Location: ${activeToken.distributorName}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        Text(
            text = "Recent Distribution History",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (orders.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No order records found.", fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("Visit the 'Order' tab to claim your subsidized grains.", fontSize = 12.sp, color = Color.LightGray)
                }
            }
        } else {
            orders.take(5).forEach { ord ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, NeutralBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = ord.orderId,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkGrayText
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Badge(
                                    containerColor = if (ord.orderStatus == "collected") SuccessGreen else InfoBlue,
                                    contentColor = Color.White
                                ) {
                                    Text(ord.orderStatus.uppercase())
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = ord.itemsText, fontSize = 13.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(text = "Ration Date: ${ord.collectionDate}", fontSize = 11.sp, color = Color.LightGray)
                        }
                        Text(
                            text = "₹${"%.2f".format(ord.totalAmount)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentSpicyOrange
                        )
                    }
                }
            }
        }
    }
}

// Helpers
fun calculatePurchasedQuantity(orders: List<RationOrder>, productType: ProductType): Double {
    var sum = 0.0
    for (o in orders) {
        if (o.orderStatus == "cancelled") continue
        val items = o.itemsText.split(";")
        for (itm in items) {
            val trimItm = itm.trim()
            if (productType == ProductType.RICE && trimItm.contains("Rice", ignoreCase = true)) {
                sum += extractDouble(trimItm)
            } else if (productType == ProductType.WHEAT && trimItm.contains("Wheat", ignoreCase = true)) {
                sum += extractDouble(trimItm)
            } else if (productType == ProductType.SUGAR && trimItm.contains("Sugar", ignoreCase = true)) {
                sum += extractDouble(trimItm)
            }
        }
    }
    return sum
}

fun extractDouble(input: String): Double {
    val regex = "([0-9.]+)".toRegex()
    val match = regex.find(input)
    return match?.value?.toDoubleOrNull() ?: 0.0
}

@Composable
fun QuotaProgressGauge(
    label: String,
    allocatedQty: Double,
    purchasedQty: Double,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val progress = if (allocatedQty > 0) (purchasedQty / allocatedQty).toFloat().coerceIn(0f, 1f) else 1f
    val remaining = (allocatedQty - purchasedQty).coerceAtLeast(0.0)

    Card(
        modifier = modifier.shadow(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))
            
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .drawBehind {
                        drawArc(
                            color = color.copy(alpha = 0.15f),
                            startAngle = 135f,
                            sweepAngle = 270f,
                            useCenter = false,
                            topLeft = Offset.Zero,
                            size = Size(size.width, size.height),
                            style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = color,
                            startAngle = 135f,
                            sweepAngle = progress * 270f,
                            useCenter = false,
                            topLeft = Offset.Zero,
                            size = Size(size.width, size.height),
                            style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = DarkGrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Used: ${"%.1f".format(purchasedQty)}/$allocatedQty $unit",
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Bal: ${"%.1f".format(remaining)} $unit",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QuickMenuCard(
    label: String,
    desc: String,
    icon: ImageVector,
    bgColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(1.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, NeutralBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(bgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = DarkGrayText, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkGrayText)
            Text(text = desc, fontSize = 10.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}


// ==========================================
// SCREEN 4: Browse Products & Shop Cart
// ==========================================
@Composable
fun BrowseOrderScreen(viewModel: RationViewModel) {
    val cart = viewModel.cartItems
    val orders by viewModel.citizenOrders.collectAsState(emptyList())

    val ricePurchased = calculatePurchasedQuantity(orders, ProductType.RICE)
    val wheatPurchased = calculatePurchasedQuantity(orders, ProductType.WHEAT)
    val sugarPurchased = calculatePurchasedQuantity(orders, ProductType.SUGAR)

    val riceRemaining = (viewModel.monthlyRiceQuota - ricePurchased).coerceAtLeast(0.0)
    val wheatRemaining = (viewModel.monthlyWheatQuota - wheatPurchased).coerceAtLeast(0.0)
    val sugarRemaining = (viewModel.monthlySugarQuota - sugarPurchased).coerceAtLeast(0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Depot Smart Catalog",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGrayText
            )
            TextButton(
                onClick = { viewModel.selectScreen(Screen.CART) },
                colors = ButtonDefaults.textButtonColors(contentColor = AccentSpicyOrange)
            ) {
                Icon(Icons.Default.ShoppingCart, null)
                Text(" View Cart (${cart.size})", fontWeight = FontWeight.Bold)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = CardDefaults.cardColors(containerColor = WarmSurface.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "⚠️ Government Subsidized Caps:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentSpicyOrange
                )
                Text(
                    text = "Avail Remainder: Rice (${"%.1f".format(riceRemaining)}kg), Wheat (${"%.1f".format(wheatRemaining)}kg), Sugar (${"%.1f".format(sugarRemaining)}kg)",
                    fontSize = 11.sp,
                    color = DarkGrayText
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SubsidizedProductsList) { prod ->
                val quantityInCart = cart[prod] ?: 0.0
                val maxLimitVal = when (prod.type) {
                    ProductType.RICE -> riceRemaining
                    ProductType.WHEAT -> wheatRemaining
                    ProductType.SUGAR -> sugarRemaining
                    else -> 99.0
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, NeutralBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(PaleWarmBackground, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = prod.iconString, fontSize = 30.sp)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = prod.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGrayText)
                            Text(text = prod.description, fontSize = 11.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "₹${"%.2f".format(prod.subsidizedPrice)} / ${prod.unit}",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = AccentSpicyOrange
                                )
                                if (prod.type == ProductType.RICE || prod.type == ProductType.WHEAT || prod.type == ProductType.SUGAR) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Badge(containerColor = InfoBlue.copy(alpha = 0.1f), contentColor = InfoBlue) {
                                        Text("Max: ${"%.1f".format(maxLimitVal)} ${prod.unit}", fontSize = 9.sp)
                                    }
                                }
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (quantityInCart == 0.0) {
                                Button(
                                    onClick = { 
                                        if (maxLimitVal > 0) viewModel.addToCart(prod, 1.0) 
                                    },
                                    enabled = maxLimitVal > 0,
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGoldenYellow, contentColor = Color.Black),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("add_to_cart_${prod.type.name}")
                                ) {
                                    Text("Add", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    IconButton(
                                        onClick = { viewModel.addToCart(prod, quantityInCart - 1.0) }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Decrease", tint = Color.Gray)
                                    }
                                    Text(
                                        text = "${quantityInCart.toInt()}",
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp),
                                        fontSize = 15.sp
                                    )
                                    IconButton(
                                        onClick = { 
                                            if (quantityInCart < maxLimitVal) {
                                                viewModel.addToCart(prod, quantityInCart + 1.0)
                                            }
                                        },
                                        enabled = quantityInCart < maxLimitVal
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = AccentSpicyOrange)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (cart.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable { viewModel.selectScreen(Screen.CART) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AccentSpicyOrange)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "${cart.size} Ration Items Selected", color = Color.White, fontSize = 12.sp)
                        Text(text = "Total Amount: ₹${"%.2f".format(viewModel.getCartTotalAmount())}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Proceed To Pay ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                    }
                }
            }
        }
    }
}


// ==========================================
// SCREEN 5: Cart & Simulated Bharat QR Code
// ==========================================
@Composable
fun CartScreen(viewModel: RationViewModel) {
    val cart = viewModel.cartItems
    var selectedDelivery by remember { mutableStateOf("pickup") } 
    var paymentModeOpen by remember { mutableStateOf(false) } 
    var purchaseSuccessAlert by remember { mutableStateOf(false) }

    val baseAmount = viewModel.getCartTotalAmount()
    val deliveryFee = if (selectedDelivery == "home-delivery") 10.00 else 0.0
    val totalAmountWithFee = baseAmount + deliveryFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.selectScreen(Screen.BROWSE_ORDER) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(text = "Ration Cart & Pay", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
        }

        if (cart.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Your cart is currently empty.", fontWeight = FontWeight.Bold, color = Color.Gray)
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = { viewModel.selectScreen(Screen.BROWSE_ORDER) }
                    ) {
                        Text("Browse Depot Grains")
                    }
                }
            }
            return
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cart.entries.toList()) { entry ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, NeutralBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = entry.key.iconString, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = entry.key.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = "Qty: ${entry.value.toInt()} ${entry.key.unit}", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        Text(
                            text = "₹${"%.2f".format(entry.key.subsidizedPrice * entry.value)}",
                            fontWeight = FontWeight.Bold,
                            color = AccentSpicyOrange,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Delivery Method", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, NeutralBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedDelivery = "pickup" },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = selectedDelivery == "pickup", onClick = { selectedDelivery = "pickup" })
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Self-Collection at Linked Depot (FPS)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Free - Book a collection token after checkout", fontSize = 11.sp, color = Color.Gray)
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedDelivery = "home-delivery" },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = selectedDelivery == "home-delivery", onClick = { selectedDelivery = "home-delivery" })
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Home-Delivery to Linked Address", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Convenience surcharge of ₹10.00 limits crowd flow", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = WarmSurface.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subsidized Grains Subtotal", fontSize = 13.sp, color = Color.Gray)
                    Text("₹${"%.2f".format(baseAmount)}", fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("PDS Convenience Charge", fontSize = 13.sp, color = Color.Gray)
                    Text("₹${"%.2f".format(deliveryFee)}", fontSize = 13.sp)
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Final Price", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkGrayText)
                    Text("₹${"%.2f".format(totalAmountWithFee)}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = AccentSpicyOrange)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { paymentModeOpen = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("checkout_pay_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen, contentColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Simulate Bharat UPI QR Payment 📲", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }

    if (paymentModeOpen) {
        AlertDialog(
            onDismissRequest = { paymentModeOpen = false },
            confirmButton = {
                Button(
                    onClick = {
                        paymentModeOpen = false
                        viewModel.checkout(selectedDelivery) { success ->
                            if (success) {
                                purchaseSuccessAlert = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen, contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Simulate Successful Receipt ✅")
                }
            },
            dismissButton = {
                TextButton(onClick = { paymentModeOpen = false }) {
                    Text("Cancel Transaction", color = Color.Red)
                }
            },
            title = {
                Text(
                    text = "BharatPDS Unified UPI Payment",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Scan using BHIM, GPay, PhonePe or Paytm to deposit ₹${"%.2f".format(totalAmountWithFee)}", fontSize = 11.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(2.dp, SecondarySaffron, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawRect(color = Color.Black, topLeft = Offset(0f, 0f), size = Size(35f, 35f))
                            drawRect(color = Color.White, topLeft = Offset(8f, 8f), size = Size(19f, 19f))
                            drawRect(color = Color.Black, topLeft = Offset(11f, 11f), size = Size(13f, 13f))

                            drawRect(color = Color.Black, topLeft = Offset(size.width - 35f, 0f), size = Size(35f, 35f))
                            drawRect(color = Color.White, topLeft = Offset(size.width - 27f, 8f), size = Size(19f, 19f))
                            drawRect(color = Color.Black, topLeft = Offset(size.width - 24f, 11f), size = Size(13f, 13f))

                            drawRect(color = Color.Black, topLeft = Offset(0f, size.height - 35f), size = Size(35f, 35f))
                            drawRect(color = Color.White, topLeft = Offset(8f, size.height - 27f), size = Size(19f, 19f))
                            drawRect(color = Color.Black, topLeft = Offset(11f, size.height - 24f), size = Size(13f, 13f))

                            val mockSegments = 10
                            val segSizeX = size.width / mockSegments
                            val segSizeY = size.height / mockSegments
                            for (i in 2 until mockSegments - 2) {
                                for (j in 2 until mockSegments - 2) {
                                    if ((i + j) % 3 == 0 || (i * j) % 4 == 1) {
                                        drawRect(
                                            color = Color.Black,
                                            topLeft = Offset(i * segSizeX, j * segSizeY),
                                            size = Size(segSizeX - 2, segSizeY - 2)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Merchant: Govt. PDS Distribution Board",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        )
    }

    if (purchaseSuccessAlert) {
        AlertDialog(
            onDismissRequest = { purchaseSuccessAlert = false },
            confirmButton = {
                Button(onClick = { 
                    purchaseSuccessAlert = false
                    viewModel.selectScreen(Screen.DASHBOARD)
                }) {
                    Text("OK, Visit Hub")
                }
            },
            title = { Text("🎉 Receipt Registered Successfully!") },
            text = {
                Text("Your subsidized order has been successfully lodged and paid. Please proceed to book an appointment (Token Slot) for express pick-up.")
            }
        )
    }
}


// ==========================================
// SCREEN 6: Token Booking (Appointment)
// ==========================================
@Composable
fun TokenBookingScreen(viewModel: RationViewModel) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedSlot by remember { mutableStateOf("") }
    var bookingConfirmationAlert by remember { mutableStateOf(false) }

    val dateList = remember {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val displaySdf = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
        val list = mutableListOf<Pair<String, String>>()
        val cal = Calendar.getInstance()
        for (i in 0..5) {
            list.add(Pair(sdf.format(cal.time), displaySdf.format(cal.time)))
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    val slots = listOf(
        "09:00 AM - 11:00 AM" to 0.75f, 
        "11:00 AM - 01:00 PM" to 0.40f, 
        "02:00 PM - 04:00 PM" to 0.15f, 
        "04:00 PM - 05:30 PM" to 0.45f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "E-Token Queue Manager 🎟️",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Pre-schedule your visit to your local Fair Price Shop to bypass standing queues completely.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text("1. Confirm Available Date", fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(bottom = 8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dateList.forEach { (rawDate, displayName) ->
                val isSelected = selectedDate == rawDate
                Card(
                    modifier = Modifier
                        .width(105.dp)
                        .clickable { selectedDate = rawDate },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) PrimaryGoldenYellow else Color.White
                    ),
                    border = BorderStroke(1.dp, if (isSelected) AccentSpicyOrange else NeutralBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = displayName.split(",")[0], fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = displayName.split(",")[1].trim(), fontSize = 11.sp, color = if (isSelected) Color.Black else Color.Gray)
                    }
                }
            }
        }

        Text("2. Choose Time Slot & Avoid Wait Peaks", fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(bottom = 8.dp))
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, NeutralBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                slots.forEach { (slotName, crowdDensity) ->
                    val isSelected = selectedSlot == slotName
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedSlot = slotName }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = isSelected, onClick = { selectedSlot = slotName })
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = slotName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                val badgeText = if (crowdDensity > 0.6f) "Critical Line" else if (crowdDensity > 0.3f) "Moderate Wait" else "Instant Collect"
                                val badgeColor = if (crowdDensity > 0.6f) ErrorRed else if (crowdDensity > 0.3f) PrimaryGoldenYellow else SuccessGreen
                                Badge(containerColor = badgeColor.copy(alpha = 0.15f), contentColor = badgeColor) {
                                    Text(" $badgeText ", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = crowdDensity,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = if (crowdDensity > 0.6f) ErrorRed else if (crowdDensity > 0.3f) PrimaryGoldenYellow else SuccessGreen,
                                trackColor = Color(0xFFF3F4F6)
                            )
                        }
                    }
                    Divider(color = NeutralBorder.copy(alpha = 0.5f))
                }
            }
        }

        Button(
            onClick = {
                if (selectedDate.isNotBlank() && selectedSlot.isNotBlank()) {
                    viewModel.bookToken(selectedDate, selectedSlot) { success ->
                        if (success) {
                            bookingConfirmationAlert = true
                        }
                    }
                }
            },
            enabled = selectedDate.isNotBlank() && selectedSlot.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_token_booking_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGoldenYellow, contentColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Generate Appointment Token 🎟️", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }

    if (bookingConfirmationAlert) {
        AlertDialog(
            onDismissRequest = { bookingConfirmationAlert = false },
            confirmButton = {
                Button(onClick = { 
                    bookingConfirmationAlert = false
                    viewModel.selectScreen(Screen.DASHBOARD)
                }) {
                    Text("Superb, Return To Dashboard")
                }
            },
            title = { Text("🎟️ Queue Slot Reserved!") },
            text = {
                Text("Your priority queue slot token has been registered in the depot database successfully. An SMS and email has been auto-relayed with verification codes.")
            }
        )
    }
}


// ==========================================
// SCREEN 7: Shop Locator & Canvas Vector Map
// ==========================================
@Composable
fun ShopLocatorScreen(viewModel: RationViewModel) {
    val distributors by viewModel.allDistributors.collectAsState(emptyList())
    var mapZoomedShopId by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Fair Price Shop Locator 🗺️",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Verify operating hours, ratings, and physical coordinates of linked ration stores.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize().background(Color(0xFFE8ECE9))) {
                    drawLine(color = Color.White, start = Offset(0f, 150f), end = Offset(size.width, 150f), strokeWidth = 16f)
                    drawLine(color = Color.White, start = Offset(180f, 0f), end = Offset(180f, size.height), strokeWidth = 16f)
                    drawLine(color = Color.White, start = Offset(size.width - 200f, 0f), end = Offset(size.width - 200f, size.height), strokeWidth = 16f)
                    
                    drawRect(
                        color = Color(0xFFCDE2C9),
                        topLeft = Offset(40f, 20f),
                        size = Size(100f, 100f)
                    )

                    drawCircle(color = AccentSpicyOrange, radius = 9f, center = Offset(180f, 150f))
                    drawCircle(color = AccentSpicyOrange.copy(alpha = 0.2f), radius = 22f, center = Offset(180f, 150f))

                    distributors.forEach { shop ->
                        val isZoomed = shop.id == mapZoomedShopId
                        val xPos = if (shop.id == 1) 180f else if (shop.id == 2) 420f else size.width - 120f
                        val yPos = if (shop.id == 1) size.height - 80f else if (shop.id == 2) 60f else size.height / 2f
                        
                        drawCircle(
                            color = if (isZoomed) PrimaryGoldenYellow else Color.White,
                            radius = if (isZoomed) 16f else 11f,
                            center = Offset(xPos, yPos),
                            style = Stroke(width = 4f)
                        )
                        drawCircle(
                            color = if (isZoomed) AccentSpicyOrange else Color.Gray,
                            radius = if (isZoomed) 11f else 6f,
                            center = Offset(xPos, yPos)
                        )
                    }
                }
                
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(4.dp))
                        .padding(4.dp)
                ) {
                    Text("🟠 Ramesh, Home   ", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("🟡 PDS Store Pins", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Text(
            text = "Identified PDS Depots nearby:",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(distributors) { shop ->
                val isSelected = shop.id == viewModel.selectedShopId
                val isZoomed = shop.id == mapZoomedShopId

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(if (isSelected) 3.dp else 1.dp)
                        .clickable { 
                            mapZoomedShopId = shop.id
                            viewModel.selectedShopId = shop.id
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) PrimaryGoldenYellow.copy(alpha = 0.11f) else Color.White
                    ),
                    border = BorderStroke(1.dp, if (isSelected) PrimaryGoldenYellow else NeutralBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = shop.shopName, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = AccentSpicyOrange, modifier = Modifier.size(16.dp))
                                Text(text = " ${shop.rating}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "📍 Address: ${shop.address}", fontSize = 11.sp, color = Color.Gray)
                        Text(text = "📞 Phone: ${shop.phone}", fontSize = 11.sp, color = Color.Gray)
                        Text(text = "🕒 Hours: ${shop.operatingHoursFrom} - ${shop.operatingHoursTo}", fontSize = 11.sp, color = Color.Gray)
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Badge(containerColor = SuccessGreen.copy(alpha = 0.15f), contentColor = SuccessGreen) {
                                    Text(" Rice ${shop.riceStock.toInt()}kg ", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Badge(containerColor = SuccessGreen.copy(alpha = 0.15f), contentColor = SuccessGreen) {
                                    Text(" Wheat ${shop.wheatStock.toInt()}kg ", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            if (isSelected) {
                                Badge(containerColor = AccentSpicyOrange, contentColor = Color.White) {
                                    Text(" LINKED TO CARD ")
                                }
                            } else {
                                TextButton(
                                    onClick = { viewModel.selectedShopId = shop.id },
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text("Link Depot", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


// ==========================================
// SCREEN 8: AI Suggestions (Gemini Powered)
// ==========================================
@Composable
fun AISuggestionsScreen(viewModel: RationViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Anna Seva AI Advisory 💡",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Personalized nutritional menus, hygiene, and grocery advice powered by server-side Gemini intelligence.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, NeutralBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Advisory Targeting Configuration", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = true, onCheckedChange = {})
                    Text("Suggest menu alternatives for school kids", fontSize = 12.sp, color = DarkGrayText)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = true, onCheckedChange = {})
                    Text("Focus on grain conservation & dry hygiene", fontSize = 12.sp, color = DarkGrayText)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.fetchAISuggestions() },
                    modifier = Modifier.fillMaxWidth().testTag("refresh_ai_suggestions_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentSpicyOrange, contentColor = Color.White)
                ) {
                    Icon(Icons.Default.Refresh, null)
                    Text(" Ask AI for Menu Enhancements ⚡")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.aiLoading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = AccentSpicyOrange)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Analyzing grocery history with Gemini AI model...", fontSize = 12.sp, color = Color.Gray)
            }
        } else {
            viewModel.aiSuggestionsResponse?.let { suggestions ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, NeutralBorder)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("✨", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Personalized PDS Nutrition Advice", 
                                fontWeight = FontWeight.Black, 
                                fontSize = 16.sp, 
                                color = AccentSpicyOrange
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        MarkdownFormatText(suggestions)
                    }
                }
            } ?: Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WarmSurface.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "Configure targets above and query the engine to receive customized health guidelines.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(24.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun MarkdownFormatText(text: String) {
    val lines = text.split("\n")
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        lines.forEach { line ->
            val cleanLine = line.trim()
            if (cleanLine.startsWith("###")) {
                Text(
                    text = cleanLine.replace("###", "").trim(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = AccentSpicyOrange,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
            } else if (cleanLine.startsWith("####")) {
                Text(
                    text = cleanLine.replace("####", "").trim(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DarkGrayText,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            } else if (cleanLine.startsWith("**") && cleanLine.endsWith("**")) {
                Text(
                    text = cleanLine.replace("**", "").trim(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DarkGrayText,
                    modifier = Modifier.padding(top = 6.dp)
                )
            } else if (cleanLine.startsWith("-") || cleanLine.startsWith("*")) {
                val bulletText = cleanLine.substring(1).trim()
                Row(modifier = Modifier.padding(start = 8.dp, top = 2.dp)) {
                    Text("• ", fontWeight = FontWeight.Bold, color = AccentSpicyOrange)
                    Text(text = bulletText, fontSize = 13.sp, color = DarkGrayText)
                }
            } else if (cleanLine.isNotBlank()) {
                Text(text = cleanLine, fontSize = 13.sp, color = DarkGrayText, lineHeight = 18.sp)
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}


// ==========================================
// SCREEN 9: Complaints System
// ==========================================
@Composable
fun ComplaintsScreen(viewModel: RationViewModel) {
    val complaints by viewModel.citizenComplaints.collectAsState(emptyList())
    
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("distributor") }
    var priority by remember { mutableStateOf("medium") }
    
    var submissionSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Assistance & Complaint Board 📝",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Raise transaction grievances or stock discrepancies directly to supervisory officers.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, NeutralBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Log A New Grievance", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkGrayText)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Grievance Category", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf("distributor" to "Shopkeeper", "quality" to "Quality", "token" to "Token Wait")
                    categories.forEach { (catId, catLabel) ->
                        FilterChip(
                            selected = category == catId,
                            onClick = { category = catId },
                            label = { Text(catLabel) }
                        )
                    }
                }

                Text("Urgency Level", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val priorities = listOf("low" to "Routine", "medium" to "Medium", "high" to "Emergency ⚠️")
                    priorities.forEach { (pId, pLabel) ->
                        FilterChip(
                            selected = priority == pId,
                            onClick = { priority = pId },
                            label = { Text(pLabel) }
                        )
                    }
                }

                Text("Grievance Title / Subject", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Short summarization") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 12.dp)
                        .testTag("complaint_title_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Text("Descriptive Grievance Explanation", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    placeholder = { Text("Detail timelines, stores, and concerns...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(top = 4.dp, bottom = 16.dp)
                        .testTag("complaint_desc_input"),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 4
                )

                Button(
                    onClick = {
                        if (title.isNotBlank() && desc.isNotBlank()) {
                            viewModel.submitComplaint(category, title, desc, priority) { success ->
                                if (success) {
                                    submissionSuccess = true
                                    title = ""
                                    desc = ""
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_complaint_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentSpicyOrange, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("File Sealed Grievance 🛡️", fontWeight = FontWeight.Bold)
                }
            }
        }

        Text(
            text = "Your Submitted Grievance Trackers",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (complaints.isEmpty()) {
            Text("No grievances lodged historically.", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        } else {
            complaints.forEach { cmp ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, NeutralBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "ID: ${cmp.complaintId}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Badge(
                                containerColor = if (cmp.status == "resolved") SuccessGreen else PrimaryGoldenYellow,
                                contentColor = Color.Black
                            ) {
                                Text(" ${cmp.status.uppercase()} ")
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Subject: ${cmp.title}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = cmp.description, fontSize = 12.sp, color = Color.Gray)
                        
                        cmp.response?.let { resp ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.08f)),
                                border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.2f))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Official Response (Supervisor GPO):", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SuccessGreen)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = resp, fontSize = 11.sp, color = DarkGrayText)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (submissionSuccess) {
        AlertDialog(
            onDismissRequest = { submissionSuccess = false },
            confirmButton = {
                Button(onClick = { submissionSuccess = false }) {
                    Text("OK")
                }
            },
            title = { Text("✅ Grievance Lodged Successfully") },
            text = {
                Text("Your complaint has been assigned a grievance ID and saved to secure PDS surveillance database.")
            }
        )
    }
}


// ==========================================
// ROLE 2: DISTRIBUTOR PORTAL SCREENS
// ==========================================

// --- DISTRIBUTOR DASHBOARD ---
@Composable
fun DistributorDashboard(viewModel: RationViewModel) {
    val shop = viewModel.currentDistributorShop ?: return
    val tokens by viewModel.distributorTokens.collectAsState(emptyList())
    val orders by viewModel.distributorOrders.collectAsState(emptyList())

    val totalPendingOrdersList = orders.filter { it.orderStatus == "placed" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(text = "Official Depot Dashboard,", fontSize = 13.sp, color = DarkGrayText.copy(alpha = 0.7f))
                Text(text = shop.shopName, fontWeight = FontWeight.Black, fontSize = 20.sp, color = DarkGrayText)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = AccentSpicyOrange, modifier = Modifier.size(16.dp))
                    Text(text = " Rating: ${shop.rating}   ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = " License No: ${shop.licenseNumber} ", fontSize = 11.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "🕒 Standard hours: ${shop.operatingHoursFrom} - ${shop.operatingHoursTo}", fontSize = 11.sp, color = Color.Gray)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DistributorMetricCard(
                label = "Pending Orders",
                value = totalPendingOrdersList.size.toString(),
                color = AccentSpicyOrange,
                modifier = Modifier.weight(1f)
            )
            DistributorMetricCard(
                label = "Active Appts",
                value = tokens.filter { it.status == "booked" }.size.toString(),
                color = SuccessGreen,
                modifier = Modifier.weight(1f)
            )
            DistributorMetricCard(
                label = "Total Fulfilled",
                value = orders.filter { it.orderStatus == "collected" }.size.toString(),
                color = InfoBlue,
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "Live Stock Status (Inventory Chart)",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, NeutralBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val stockList = listOf(
                    Triple("Rice", shop.riceStock, 2000.0),
                    Triple("Wheat", shop.wheatStock, 2000.0),
                    Triple("Sugar", shop.sugarStock, 1000.0),
                    Triple("Atta", shop.flourStock, 1000.0),
                    Triple("Soya Oil", shop.oilStock, 500.0)
                )

                stockList.forEach { (name, stock, target) ->
                    val proportion = (stock / target).toFloat().coerceIn(0f, 1f)
                    val warningThreshold = target * 0.15f

                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Row {
                                Text(
                                    text = "${stock.toInt()} kg/L",
                                    fontWeight = FontWeight.Bold,
                                    color = if (stock <= warningThreshold) ErrorRed else DarkGrayText,
                                    fontSize = 13.sp
                                )
                                Text(text = " (Cap: ${target.toInt()})", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = proportion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (stock <= warningThreshold) ErrorRed else PrimaryGoldenYellow,
                            trackColor = Color(0xFFF3F4F6)
                        )
                        if (stock <= warningThreshold) {
                            Text(text = "⚠️ Stock critically low! Restock soon.", fontSize = 9.sp, color = ErrorRed, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        val isLowStock = shop.riceStock < 200.0 || shop.wheatStock < 200.0
        if (isLowStock) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.08f)),
                border = BorderStroke(1.dp, ErrorRed)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📢 ", fontSize = 20.sp)
                    Text("Critical warning: Rice or wheat balances fell below 200kg safety levels.", fontSize = 11.sp, color = ErrorRed, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DistributorMetricCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, NeutralBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}


// --- DISTRIBUTOR STOCK ADJUSTMENT ---
@Composable
fun DistributorStockScreen(viewModel: RationViewModel) {
    val shop = viewModel.currentDistributorShop ?: return
    var selectedProdKey by remember { mutableStateOf(ProductType.RICE) }
    var restockAmountTxt by remember { mutableStateOf("500") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Depot Stock Adjuster ⚙️",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Re-stock item allocations instantly into system upon verification as valid logs.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, NeutralBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Product To Replenish", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                
                ProductType.values().forEach { prod ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedProdKey = prod }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selectedProdKey == prod, onClick = { selectedProdKey = prod })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = prod.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        val stockVal = when (prod) {
                            ProductType.RICE -> shop.riceStock
                            ProductType.WHEAT -> shop.wheatStock
                            ProductType.SUGAR -> shop.sugarStock
                            ProductType.FLOUR -> shop.flourStock
                            ProductType.OIL -> shop.oilStock
                        }
                        Text(text = "${stockVal.toInt()} Available", fontSize = 12.sp, color = Color.Gray)
                    }
                    Divider(color = NeutralBorder.copy(alpha = 0.5f))
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                Text(text = "Amount To Add (Kg / Liters)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                OutlinedTextField(
                    value = restockAmountTxt,
                    onValueChange = { restockAmountTxt = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 16.dp)
                        .testTag("restock_amount_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Button(
                    onClick = {
                        val amt = restockAmountTxt.toDoubleOrNull() ?: 0.0
                        if (amt > 0) {
                            viewModel.restockProduct(selectedProdKey, amt)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("restock_submit_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentSpicyOrange, contentColor = Color.White)
                ) {
                    Text("Increment Live Stock 🚀", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


// --- DISTRIBUTOR ORDERS LOG & OPERATION FULFILLMENT ---
@Composable
fun DistributorOrdersScreen(viewModel: RationViewModel) {
    val orders by viewModel.distributorOrders.collectAsState(emptyList())
    val pendingOrders = orders.filter { it.orderStatus == "placed" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Order Release & Logistics",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Select active customer orders and confirm grain hand over upon verifying details.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (pendingOrders.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉 All orders cleared!", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SuccessGreen)
                    Text("No pending citizens pick-ups left.", fontSize = 12.sp, color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(pendingOrders) { ord ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, NeutralBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = ord.orderId, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                Badge(containerColor = AccentSpicyOrange, contentColor = Color.White) {
                                    Text(" ${ord.collectionType.uppercase()} ")
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Cardholder: ${ord.cardholderName}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "Ration ID: ${ord.cardNumber}", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "Requested Grains: ${ord.itemsText}", fontSize = 13.sp, color = DarkGrayText)
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total Collected: ₹${"%.2f".format(ord.totalAmount)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = SuccessGreen
                                )
                                Button(
                                    onClick = { viewModel.markOrderDistributed(ord) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen, contentColor = Color.White),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("fulfill_order_${ord.orderId}")
                                ) {
                                    Text("Verify & Hand over 🌾", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


// --- DISTRIBUTOR SLOTS MANAGEMENT ---
@Composable
fun DistributorSlotsScreen(viewModel: RationViewModel) {
    val tokens by viewModel.distributorTokens.collectAsState(emptyList())
    val activeTokens = tokens.filter { it.status == "booked" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Pre-Booked Time Slots 🕒",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Review tomorrow's scheduled citizen arrivals to proactively pack grain packets.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (activeTokens.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("No priority appointments booked currently.", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(activeTokens) { token ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, NeutralBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .background(PrimaryGoldenYellow, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎟️", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Token: ${token.tokenNumber}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(text = "Ration ID: ${token.cardNumber} (${token.cardholderName})", fontSize = 12.sp, color = Color.Gray)
                                Text(text = "Required: ${token.itemsText}", fontSize = 11.sp, color = Color.Gray)
                                Text(
                                    text = "Arrival: ${token.bookingDate} (${token.collectionTimeSlot})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentSpicyOrange
                                )
                            }
                            IconButton(onClick = { viewModel.resolveToken(token, "collected") }) {
                                Icon(Icons.Default.Check, "Done", tint = SuccessGreen)
                            }
                        }
                    }
                }
            }
        }
    }
}


// --- DISTRIBUTOR COMPLAINTS ---
@Composable
fun DistributorComplaintsScreen(viewModel: RationViewModel) {
    val complaints by viewModel.citizenComplaints.collectAsState(emptyList())

    var resolverText by remember { mutableStateOf("") }
    var selectedComplaintForResponse by remember { mutableStateOf<Complaint?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Depot Security Alerts 👮",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGrayText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Review notifications, complaints or regulatory warnings issued regarding your FPS outlet.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val outstandingComplaints = complaints.filter { it.status == "submitted" }

        if (outstandingComplaints.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🙌 Perfect Compliance Score!", fontWeight = FontWeight.Bold, color = SuccessGreen, fontSize = 16.sp)
                    Text("Zero active complaints registered against your license.", fontSize = 12.sp, color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(outstandingComplaints) { comp ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, NeutralBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = comp.complaintId, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Badge(containerColor = ErrorRed, contentColor = Color.White) {
                                    Text(" ${comp.priority.uppercase()} ")
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Filer: ${comp.cardholderName} (${comp.cardNumber})", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "Subject: ${comp.title}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = comp.description, fontSize = 12.sp, color = Color.Gray)
                            
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            val isSelected = selectedComplaintForResponse?.id == comp.id
                            if (isSelected) {
                                OutlinedTextField(
                                    value = resolverText,
                                    onValueChange = { resolverText = it },
                                    placeholder = { Text("Describe resolution details...") },
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(), 
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = { selectedComplaintForResponse = null }) {
                                        Text("Cancel")
                                    }
                                    Button(
                                        onClick = {
                                            if (resolverText.isNotBlank()) {
                                                viewModel.resolveComplaint(comp, resolverText)
                                                resolverText = ""
                                                selectedComplaintForResponse = null
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                                    ) {
                                        Text("Submit Solution")
                                    }
                                }
                            } else {
                                Button(
                                    onClick = { selectedComplaintForResponse = comp },
                                    modifier = Modifier.align(Alignment.End),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGoldenYellow, contentColor = Color.Black)
                                ) {
                                    Text("Lodge Official Response", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
