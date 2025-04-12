
package com.example.myapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue





@Composable
fun CartScreen(
    navController: NavController,
    viewModel: ShopViewModel
) {
    val cartItems by viewModel.cartItems.collectAsState(initial = emptyList())
    val gpus by viewModel.allGPUs.collectAsState(initial = emptyList())
    val currentUserId by viewModel.currentUserId.collectAsState()
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    val cartItemsWithGPU = remember(cartItems, gpus) {
        cartItems.map { cartItem ->
            val gpu = gpus.find { it.id == cartItem.gpuId } ?: GPU.empty()
            Pair(cartItem, gpu)
        }
    }

    val totalPrice = remember(cartItemsWithGPU) {
        cartItemsWithGPU.sumOf { (cartItem, gpu) -> cartItem.quantity * gpu.price }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, currentUserId, currentRoute)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (cartItemsWithGPU.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Корзина пуста",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Корзина пуста",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItemsWithGPU) { (cartItem, gpu) ->
                        CartItemCard(
                            gpu = gpu,
                            quantity = cartItem.quantity,
                            onIncrease = { viewModel.addToCart(gpu.id) },
                            onDecrease = { viewModel.removeFromCart(gpu.id) },
                            onRemove = { viewModel.deleteCartItem(cartItem) },
                            onItemClick = { navController.navigate(Screen.Item.createRoute(gpu.id)) }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Итого:",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "%.2f ₽".format(totalPrice),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.clearCart() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("Очистить корзину")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

//                    Button(
//                        onClick = { /* Оформление заказа */ },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Оформить заказ")
//                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    gpu: GPU,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onItemClick() }
            ) {
//                Image(
//                    painter = rememberImagePainter(gpu.imageUrl),
//                    contentDescription = gpu.name,
//                    modifier = Modifier
//                        .size(60.dp)
//                        .clip(RoundedCornerShape(8.dp))
//                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = gpu.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "%.2f ₽".format(gpu.price),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDecrease,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
                    }

                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = onIncrease,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Увеличить")
                    }
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.Red)
                }
            }
        }
    }
}