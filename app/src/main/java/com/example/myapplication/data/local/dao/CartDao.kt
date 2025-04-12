
package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItems(userId: Int): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND gpuId = :gpuId")
    suspend fun getCartItem(userId: Int, gpuId: Int): CartItem?

    @Insert
    suspend fun insertCartItem(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Int)

    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    fun getCartItemCount(userId: Int): Flow<Int>

    @Query("DELETE FROM cart_items")
    suspend fun clearAllCartItems()
}