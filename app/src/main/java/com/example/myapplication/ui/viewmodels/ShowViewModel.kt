package com.example.myapplication


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val repository: ShopRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId: StateFlow<Int?> = _currentUserId.asStateFlow()

    val allGPUs = repository.getAllGPUs()


    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: Flow<List<CartItem>> = currentUserId.flatMapLatest { userId ->
        if (userId == null) flowOf(emptyList())
        else repository.getCartItems(userId)
    }

    val cartItemCount = currentUserId.flatMapLatest { userId ->
        if (userId == null) flowOf(0)
        else repository.getCartItemCount(userId)
    }

    init {

        viewModelScope.launch {
            sessionManager.getUserId().collect { savedUserId ->
                _currentUserId.value = savedUserId

                savedUserId?.let { userId ->
                    repository.getCartItems(userId).collect { items ->
                        _cartItems.value = items
                    }
                }
            }

        }
    }

    fun loginUser(
        email: String,
        password: String,
        onSuccess: (Int) -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            val user = repository.getUser(email, password)
            if (user != null) {
                _currentUserId.value = user.id
                sessionManager.saveUserId(user.id)
                onSuccess(user.id)
            } else {
                onFailure()
            }
        }
    }

    fun registerUser(username: String, email: String, password: String, onSuccess: (Int) -> Unit,  onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val existingUser = repository.getUserByEmail(email)
            if (existingUser != null) {
                onFailure("Пользователь с таким email уже существует")
                return@launch
            } else {


                val user = User(username = username, email = email, password = password)
                repository.insertUser(user)
                val registeredUser = repository.getUser(email, password)
                registeredUser?.id?.let { userId ->
                    _currentUserId.value = userId
                    sessionManager.saveUserId(userId)
                    onSuccess(userId)
                }?: onFailure("Не удалось зарегистрировать пользователя")

            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _currentUserId.value = null
            sessionManager.clear()
        }
    }


    fun addToCart(gpuId: Int) {
        viewModelScope.launch {
            val userId = _currentUserId.value ?: return@launch
            repository.addToCart(userId, gpuId)
        }

    }

    fun removeFromCart(gpuId: Int) {

        viewModelScope.launch {
            val userId = _currentUserId.value ?: return@launch
            repository.removeFromCart(userId, gpuId)

        }

    }

    fun deleteCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            repository.deleteCartItem(cartItem)
        }
    }

    fun clearCart() {
        _currentUserId.value?.let { userId ->
            viewModelScope.launch {
                repository.clearCart(userId)
            }
        }
    }

    fun isInCart(gpuId: Int): Flow<Boolean> {
        return cartItems.map { items -> items.any { it.gpuId == gpuId } }
    }

    fun getCartItemQuantity(gpuId: Int): Int {
        return _cartItems.value.find { it.gpuId == gpuId }?.quantity ?: 0
    }

    fun getGPUById(gpuId: Int): Flow<GPU> {
        return repository.getGPUById(gpuId)
    }

    fun getUserById(id: Int): Flow<User?> {
        return if (id == 0) flowOf(null) else repository.getUserById(id)
    }



}