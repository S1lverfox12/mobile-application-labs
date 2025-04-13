package com.example.myapplication



import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Login
import androidx.navigation.NavController



@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentUserId: Int?,
    currentRoute: String?
) {
    val items = listOf(
        Screen.Main,
        if (currentUserId != null) Screen.Cart else null,
        if (currentUserId != null) Screen.Profile else Screen.Login
    ).filterNotNull()

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (screen) {
                        is Screen.Main -> Icon(Icons.Default.Home, contentDescription = "Главная")
                        is Screen.Cart -> Icon(Icons.Default.ShoppingCart, contentDescription = "Корзина")
                        is Screen.Profile -> Icon(Icons.Default.Person, contentDescription = "Профиль")
                        is Screen.Login -> Icon(Icons.Default.Login, contentDescription = "Войти")
                        else -> Icon(Icons.Default.Home, contentDescription = "Главная")
                    }
                },
                label = {
                    when (screen) {
                        is Screen.Main -> Text("Главная")
                        is Screen.Cart -> Text("Корзина")
                        is Screen.Profile -> Text("Профиль")
                        is Screen.Login -> Text("Войти")
                        else -> Text("Главная")
                    }
                },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
//                )
            )
        }
    }
}