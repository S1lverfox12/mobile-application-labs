package com.example.myapplication


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String
) {
    companion object {
        fun empty() = User(
            username = "",
            email = "",
            password = ""
        )
    }
}

@Entity(tableName = "gpus")
data class GPU(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val manufacturer: String,
    val price: Double,
    val imageUrl: String,
    val memorySize: Int,
    val memoryType: String,
    val coreClock: Int,
    val boostClock: Int,
    val cudaCores: Int,
    val description: String
) {
    companion object {
        fun empty() = GPU(
            name = "",
            manufacturer = "",
            price = 0.0,
            imageUrl = "",
            memorySize = 0,
            memoryType = "",
            coreClock = 0,
            boostClock = 0,
            cudaCores = 0,
            description = ""
        )
    }
}

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GPU::class,
            parentColumns = ["id"],
            childColumns = ["gpuId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val gpuId: Int,
    var quantity: Int = 1
)