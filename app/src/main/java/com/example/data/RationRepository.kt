package com.example.data

import kotlinx.coroutines.flow.Flow

class RationRepository(private val rationDao: RationDao) {

    // --- User Operations ---
    suspend fun registerUser(user: User): Long {
        return rationDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return rationDao.getUserByEmail(email)
    }

    suspend fun getUserByCardNumber(cardNumber: String): User? {
        return rationDao.getUserByCardNumber(cardNumber)
    }

    fun getUserByIdFlow(id: Int): Flow<User?> {
        return rationDao.getUserByIdFlow(id)
    }


    // --- Distributor Operations ---
    suspend fun registerDistributor(distributor: FPSDistributor): Long {
        return rationDao.insertDistributor(distributor)
    }

    fun getAllDistributorsFlow(): Flow<List<FPSDistributor>> {
        return rationDao.getAllDistributorsFlow()
    }

    suspend fun getDistributorById(id: Int): FPSDistributor? {
        return rationDao.getDistributorById(id)
    }

    suspend fun getDistributorByLicense(licenseNumber: String): FPSDistributor? {
        return rationDao.getDistributorByLicense(licenseNumber)
    }

    suspend fun updateDistributor(distributor: FPSDistributor) {
        rationDao.updateDistributor(distributor)
    }


    // --- Token Operations ---
    suspend fun bookToken(token: Token): Long {
        return rationDao.insertToken(token)
    }

    fun getTokensByCardNumberFlow(cardNumber: String): Flow<List<Token>> {
        return rationDao.getTokensByCardNumberFlow(cardNumber)
    }

    fun getTokensByDistributorFlow(distributorId: Int): Flow<List<Token>> {
        return rationDao.getTokensByDistributorFlow(distributorId)
    }

    suspend fun updateToken(token: Token) {
        rationDao.updateToken(token)
    }

    suspend fun getTokenById(id: Int): Token? {
        return rationDao.getTokenById(id)
    }


    // --- Order Operations ---
    suspend fun placeOrder(order: RationOrder): Long {
        return rationDao.insertOrder(order)
    }

    fun getOrdersByCardNumberFlow(cardNumber: String): Flow<List<RationOrder>> {
        return rationDao.getOrdersByCardNumberFlow(cardNumber)
    }

    fun getOrdersByDistributorFlow(distributorId: Int): Flow<List<RationOrder>> {
        return rationDao.getOrdersByDistributorFlow(distributorId)
    }

    suspend fun updateOrder(order: RationOrder) {
        rationDao.updateOrder(order)
    }

    suspend fun getOrderById(id: Int): RationOrder? {
        return rationDao.getOrderById(id)
    }


    // --- Complaint Operations ---
    suspend fun submitComplaint(complaint: Complaint): Long {
        return rationDao.insertComplaint(complaint)
    }

    fun getComplaintsByCardNumberFlow(cardNumber: String): Flow<List<Complaint>> {
        return rationDao.getComplaintsByCardNumberFlow(cardNumber)
    }

    suspend fun getComplaintById(id: Int): Complaint? {
        return rationDao.getComplaintById(id)
    }

    suspend fun updateComplaint(complaint: Complaint) {
        rationDao.updateComplaint(complaint)
    }
}
