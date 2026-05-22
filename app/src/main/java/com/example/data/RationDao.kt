package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RationDao {

    // --- User Queries ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE cardNumber = :cardNumber LIMIT 1")
    suspend fun getUserByCardNumber(cardNumber: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserByIdFlow(id: Int): Flow<User?>


    // --- Distributor Queries ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistributor(distributor: FPSDistributor): Long

    @Query("SELECT * FROM distributors")
    fun getAllDistributorsFlow(): Flow<List<FPSDistributor>>

    @Query("SELECT * FROM distributors")
    suspend fun getAllDistributors(): List<FPSDistributor>

    @Query("SELECT * FROM distributors WHERE id = :id LIMIT 1")
    suspend fun getDistributorById(id: Int): FPSDistributor?

    @Query("SELECT * FROM distributors WHERE licenseNumber = :licenseNumber LIMIT 1")
    suspend fun getDistributorByLicense(licenseNumber: String): FPSDistributor?

    @Update
    suspend fun updateDistributor(distributor: FPSDistributor)


    // --- Token Queries ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: Token): Long

    @Query("SELECT * FROM tokens WHERE cardNumber = :cardNumber ORDER BY id DESC")
    fun getTokensByCardNumberFlow(cardNumber: String): Flow<List<Token>>

    @Query("SELECT * FROM tokens WHERE distributorId = :distributorId ORDER BY id DESC")
    fun getTokensByDistributorFlow(distributorId: Int): Flow<List<Token>>

    @Query("SELECT * FROM tokens WHERE id = :id LIMIT 1")
    suspend fun getTokenById(id: Int): Token?

    @Update
    suspend fun updateToken(token: Token)


    // --- Order Queries ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: RationOrder): Long

    @Query("SELECT * FROM orders WHERE cardNumber = :cardNumber ORDER BY timestamp DESC")
    fun getOrdersByCardNumberFlow(cardNumber: String): Flow<List<RationOrder>>

    @Query("SELECT * FROM orders WHERE distributorId = :distributorId ORDER BY timestamp DESC")
    fun getOrdersByDistributorFlow(distributorId: Int): Flow<List<RationOrder>>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getOrderById(id: Int): RationOrder?

    @Update
    suspend fun updateOrder(order: RationOrder)


    // --- Complaint Queries ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: Complaint): Long

    @Query("SELECT * FROM complaints WHERE cardNumber = :cardNumber ORDER BY id DESC")
    fun getComplaintsByCardNumberFlow(cardNumber: String): Flow<List<Complaint>>

    @Query("SELECT * FROM complaints WHERE id = :id LIMIT 1")
    suspend fun getComplaintById(id: Int): Complaint?

    @Update
    suspend fun updateComplaint(complaint: Complaint)
}
