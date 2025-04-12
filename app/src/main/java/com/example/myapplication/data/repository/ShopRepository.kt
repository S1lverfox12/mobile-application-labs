package com.example.myapplication
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val gpuDao: GPUDao,
    private val userDao: UserDao,
    private val cartDao: CartDao
) {
    // GPU operations
    fun getAllGPUs(): Flow<List<GPU>> = gpuDao.getAllGPUs()
    fun getGPUById(gpuId: Int): Flow<GPU> = gpuDao.getGPUById(gpuId)
    suspend fun insertGPU(gpu: GPU) = gpuDao.insertGPU(gpu)

    // User operations
    suspend fun getUser(email: String, password: String) = userDao.getUser(email, password)
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    fun getUserById(userId: Int): Flow<User> = userDao.getUserById(userId)

    // Cart operations
    fun getCartItems(userId: Int): Flow<List<CartItem>> = cartDao.getCartItems(userId)
    suspend fun getCartItem(userId: Int, gpuId: Int) = cartDao.getCartItem(userId, gpuId)
    suspend fun addToCart(userId: Int, gpuId: Int) {
        val existingItem = cartDao.getCartItem(userId, gpuId)
        if (existingItem != null) {
            existingItem.quantity += 1
            cartDao.updateCartItem(existingItem)
        } else {
            cartDao.insertCartItem(CartItem(userId = userId, gpuId = gpuId))
        }
    }
    suspend fun removeFromCart(userId: Int, gpuId: Int) {
        val existingItem = cartDao.getCartItem(userId, gpuId)
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity -= 1
                cartDao.updateCartItem(existingItem)
            } else {
                cartDao.deleteCartItem(existingItem)
            }
        }
    }
    suspend fun deleteCartItem(cartItem: CartItem) = cartDao.deleteCartItem(cartItem)
    suspend fun clearCart(userId: Int) = cartDao.clearCart(userId)
    fun getCartItemCount(userId: Int): Flow<Int> = cartDao.getCartItemCount(userId)
}