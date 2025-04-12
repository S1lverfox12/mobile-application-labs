package com.example.myapplication


sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Item : Screen("item/{gpuId}") {
        fun createRoute(gpuId: Int) = "item/$gpuId"
    }
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
    object About: Screen("about")
}