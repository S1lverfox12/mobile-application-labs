
package com.example.myapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController



@Composable
fun MainScreen(
    navController: NavController,
    viewModel: ShopViewModel
) {
    val gpus by viewModel.allGPUs.collectAsState(initial = emptyList())
    val cartItems by viewModel.cartItems.collectAsState(initial = emptyList())
    val currentUserId by viewModel.currentUserId.collectAsState()
    val currentRoute = navController.currentBackStackEntry?.destination?.route



    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, currentUserId, currentRoute)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(gpus) { gpu ->
                val isInCart by viewModel.isInCart(gpu.id).collectAsState(false)
                val cartItem = cartItems.find { it.gpuId == gpu.id }
                GPUCard(
                    gpu = gpu,
                    isInCart = isInCart,
                    onAddToCart = { viewModel.addToCart(gpu.id) },
                    onRemoveFromCart = { cartItem?.let { viewModel.deleteCartItem(it) }},
                    onItemClick = { navController.navigate(Screen.Item.createRoute(gpu.id)) }
                )
            }
        }
    }
}