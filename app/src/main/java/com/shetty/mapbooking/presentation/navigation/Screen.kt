package com.shetty.mapbooking.presentation.navigation

sealed class Screen(
    val route: String
) {

    data object Map : Screen("map")

    data object Location : Screen("location/{type}") {

        fun createRoute(type: String): String {
            return "location/$type"
        }
    }

    data object Booking : Screen("booking")

    data object History : Screen("history")
}