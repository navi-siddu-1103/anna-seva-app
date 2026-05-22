package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        FPSDistributor::class,
        Token::class,
        RationOrder::class,
        Complaint::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun rationDao(): RationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "anna_seva_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.rationDao())
                }
            }
        }

        suspend fun populateDatabase(dao: RationDao) {
            // Check if databases are empty first
            val distributors = dao.getAllDistributors()
            if (distributors.isEmpty()) {
                // Seed Distributors (FPS Shops)
                dao.insertDistributor(
                    FPSDistributor(
                        id = 1,
                        shopName = "Sri Sai Fair Price Shop - Shop #102",
                        ownerName = "Rajesh Kumar",
                        phone = "+91 98765 43210",
                        licenseNumber = "FPS-2015-88432",
                        address = "Main Market Road, Sector 4, New Delhi",
                        lat = 28.59,
                        lng = 77.21,
                        rating = 4.3f,
                        riceStock = 1250.0,
                        wheatStock = 1500.0,
                        sugarStock = 350.0,
                        flourStock = 500.0,
                        oilStock = 200.0,
                        operatingHoursFrom = "08:30",
                        operatingHoursTo = "17:30"
                    )
                )

                dao.insertDistributor(
                    FPSDistributor(
                        id = 2,
                        shopName = "Janta Seva Kendra FPS - Shop #405",
                        ownerName = "Sita Ram Patel",
                        phone = "+91 87654 32109",
                        licenseNumber = "FPS-2018-12940",
                        address = "Near Durga Temple, Ward 12, Jaipur, Rajasthan",
                        lat = 26.91,
                        lng = 75.78,
                        rating = 4.7f,
                        riceStock = 1800.0,
                        wheatStock = 2100.0,
                        sugarStock = 600.0,
                        flourStock = 800.0,
                        oilStock = 450.0,
                        operatingHoursFrom = "09:00",
                        operatingHoursTo = "18:00"
                    )
                )

                dao.insertDistributor(
                    FPSDistributor(
                        id = 3,
                        shopName = "Apna Ration Kalyan Depot",
                        ownerName = "Vikram Sharma",
                        phone = "+91 76543 21098",
                        licenseNumber = "FPS-2020-55210",
                        address = "Chowk Bazar, Gwalior, Madhya Pradesh",
                        lat = 26.21,
                        lng = 78.17,
                        rating = 3.9f,
                        riceStock = 450.0,  // low stock!
                        wheatStock = 600.0,
                        sugarStock = 50.0,
                        flourStock = 300.0,
                        oilStock = 80.0,
                        operatingHoursFrom = "09:00",
                        operatingHoursTo = "19:00"
                    )
                )

                // Seed Default Demo Users for easier testing & evaluation
                // 1. Citizen test account
                dao.insertUser(
                    User(
                        id = 1,
                        name = "Ramesh Chand",
                        email = "ramesh@example.com",
                        phone = "9876543210",
                        role = "citizen",
                        cardNumber = "RC-DEL-104928A",
                        licenseNumber = null,
                        status = "active"
                    )
                )

                // 2. Distributor test account
                dao.insertUser(
                    User(
                        id = 2,
                        name = "Rajesh Kumar",
                        email = "rajesh@example.com",
                        phone = "8888888888",
                        role = "distributor",
                        cardNumber = null,
                        licenseNumber = "FPS-2015-88432",
                        status = "active"
                    )
                )

                // Seed some initial demo complaints
                dao.insertComplaint(
                    Complaint(
                        complaintId = "COMP-72910",
                        cardNumber = "RC-DEL-104928A",
                        cardholderName = "Ramesh Chand",
                        category = "distributor",
                        title = "FPS closed early during afternoon session",
                        description = "I visited the store at 3:30 PM on May 15th, but the dealer shut down the shop saying they ran out of daily print receipts. The operating hours are display as open until 5:30 PM.",
                        status = "resolved",
                        priority = "medium",
                        response = "Apologies for the inconvenience. We have warned the FPS owner to adhere strictly to the declared timings. A backup receipt printer has been provisioned.",
                        resolutionDate = "2026-05-18",
                        createdAt = "2026-05-15 16:10"
                    )
                )
            }
        }
    }
}
