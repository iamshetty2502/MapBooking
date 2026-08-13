package com.shetty.mapbooking.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shetty.mapbooking.presentation.screen.map.MapScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Map.route
    ) {

        composable (
            route = Screen.Map.route
        ) {

            MapScreen(
                onNavigate = { event ->
                    when (event) {

                        NavigationEvent.LocationA -> {
                            navController.navigate(
                                Screen.Location.createRoute("A")
                            )
                        }

                        NavigationEvent.LocationB -> {
                            navController.navigate(
                                Screen.Location.createRoute("B")
                            )
                        }

                        NavigationEvent.Booking -> {
                            navController.navigate(
                                Screen.Booking.route
                            )
                        }

                        NavigationEvent.History -> {
                            navController.navigate(
                                Screen.History.route
                            )
                        }

                        NavigationEvent.BackToMap -> {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.Location.route
        ) {
            TemporaryLocationScreen()
        }

        composable(
            route = Screen.Booking.route
        ) {
            TemporaryBookingScreen()
        }

        composable(
            route = Screen.History.route
        ) {
            TemporaryHistoryScreen()
        }
    }
}

@Composable
private fun TemporaryLocationScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Location Details")
    }
}

@Composable
private fun TemporaryBookingScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Booking")
    }
}

@Composable
private fun TemporaryHistoryScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("History")
    }
}