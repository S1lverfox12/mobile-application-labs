
package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: ShopViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController, viewModel)
        }
        composable(
            route = Screen.Item.route,
            arguments = listOf(navArgument("gpuId") { type = NavType.IntType })
        ) { backStackEntry ->
            val gpuId = backStackEntry.arguments?.getInt("gpuId") ?: 0
            ItemScreen(navController, viewModel, gpuId)
        }
        composable(Screen.Cart.route) {
            CartScreen(navController, viewModel)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController, viewModel)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController, viewModel)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController, viewModel)
        }
        composable(Screen.About.route) {
            AboutScreen(navController)
        }
    }
}