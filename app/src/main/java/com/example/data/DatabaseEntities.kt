package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val role: String, // "citizen" or "distributor"
    val cardNumber: String? = null,
    val licenseNumber: String? = null,
    val status: String = "active"
)

@Entity(tableName = "distributors")
data class FPSDistributor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val shopName: String,
    val ownerName: String,
    val phone: String,
    val licenseNumber: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val rating: Float,
    val riceStock: Double,  // in kg
    val wheatStock: Double, // in kg
    val sugarStock: Double, // in kg
    val flourStock: Double, // in kg
    val oilStock: Double,   // in liters
    val operatingHoursFrom: String = "09:00",
    val operatingHoursTo: String = "18:00"
)

@Entity(tableName = "tokens")
data class Token(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tokenNumber: String,
    val cardNumber: String,
    val cardholderName: String,
    val distributorId: Int,
    val distributorName: String,
    val itemsText: String, // format: "Rice: 5kg, Wheat: 5kg"
    val status: String, // "booked", "collected", "cancelled"
    val bookingDate: String, // "YYYY-MM-DD"
    val collectionTimeSlot: String, // "10:00 - 11:00"
    val cancelReason: String? = null
)

@Entity(tableName = "orders")
data class RationOrder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: String,
    val cardNumber: String,
    val cardholderName: String,
    val distributorId: Int,
    val shopName: String,
    val itemsText: String, // JSON or formatted text
    val totalAmount: Double,
    val paymentStatus: String, // "pending", "completed", "failed"
    val orderStatus: String, // "placed", "ready", "collected", "cancelled"
    val collectionType: String, // "pickup" or "home-delivery"
    val collectionDate: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "complaints")
data class Complaint(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val complaintId: String,
    val cardNumber: String,
    val cardholderName: String,
    val category: String, // "order", "token", "distributor", "quality", "other"
    val title: String,
    val description: String,
    val status: String, // "submitted", "under-review", "resolved", "closed"
    val priority: String, // "low", "medium", "high"
    val response: String? = null,
    val resolutionDate: String? = null,
    val createdAt: String
)
