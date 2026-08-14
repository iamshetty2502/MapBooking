package com.shetty.mapbooking.presentation.navigation

import android.net.Uri

sealed class Screen(
    val route: String
) {

    data object Map : Screen(
        route = "map"
    )

    data object Location : Screen(
        route = "location/{type}/{latitude}/{longitude}/{aqi}/{name}"
    ) {

        fun createRoute(
            type: String,
            latitude: Double,
            longitude: Double,
            aqi: Int,
            name: String
        ): String {

            return "location/" +
                    "${Uri.encode(type)}/" +
                    "$latitude/" +
                    "$longitude/" +
                    "$aqi/" +
                    Uri.encode(name)
        }
    }

    data object Booking : Screen(
        route = "booking"
    )

    data object History : Screen(
        route = "history"
    )
}