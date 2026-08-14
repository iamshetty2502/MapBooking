package com.shetty.mapbooking.presentation.navigation

import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shetty.mapbooking.presentation.screen.booking.BookingScreen
import com.shetty.mapbooking.presentation.screen.history.HistoryScreen
import com.shetty.mapbooking.presentation.screen.location.LocationScreen
import com.shetty.mapbooking.presentation.screen.map.MapScreen
import com.shetty.mapbooking.presentation.screen.map.MapViewModel

@Composable
fun AppNavigation() {

    // =========================================================
    // NAVIGATION CONTROLLER
    // =========================================================

    val navController = rememberNavController()

    val currentBackStackEntry by
    navController.currentBackStackEntryAsState()


    // =========================================================
    // MAP VIEW MODEL
    // =========================================================

    /*
     * We keep the MapViewModel at the navigation level so that
     * the same ViewModel instance survives:
     *
     * Screen 1 → Screen 2 → Screen 1
     *
     * This is important because A/B locations and nicknames
     * belong to the Map screen state.
     */
    val mapViewModel: MapViewModel =
        hiltViewModel()


    // =========================================================
    // SCREEN 2 RESULT
    // =========================================================

    /*
     * Screen 2 sends the nickname back using
     * SavedStateHandle before popping back to Screen 1.
     *
     * When Screen 1 becomes the current destination again,
     * this effect reads the result and updates MapViewModel.
     */

    LaunchedEffect(currentBackStackEntry) {

        if (
            currentBackStackEntry?.destination?.route
            == Screen.Map.route
        ) {

            val savedStateHandle =
                currentBackStackEntry
                    ?.savedStateHandle

            val locationType =
                savedStateHandle
                    ?.get<String>("location_type")

            val nickname =
                savedStateHandle
                    ?.get<String>("location_nickname")

            if (locationType != null) {

                mapViewModel.updateNickname(
                    type = locationType,
                    nickname = nickname.orEmpty()
                )

                // Remove the result after consuming it.
                savedStateHandle.remove<String>(
                    "location_type"
                )

                savedStateHandle.remove<String>(
                    "location_nickname"
                )
            }
        }
    }


    // =========================================================
    // NAV HOST
    // =========================================================

    NavHost(
        navController = navController,
        startDestination = Screen.Map.route
    ) {


        // =====================================================
        // SCREEN 1 — MAP
        // =====================================================

        composable(
            route = Screen.Map.route
        ) {

            MapScreen(
                viewModel = mapViewModel,

                onNavigate = { event ->

                    when (event) {

                        // -------------------------------------
                        // A LOCATION
                        // -------------------------------------

                        is NavigationEvent.LocationA -> {

                            navController.navigate(
                                Screen.Location.createRoute(
                                    type = "A",
                                    latitude =
                                        event.location.latitude,
                                    longitude =
                                        event.location.longitude,
                                    aqi =
                                        event.location.aqi,
                                    name =
                                        event.location.name
                                )
                            )
                        }


                        // -------------------------------------
                        // B LOCATION
                        // -------------------------------------

                        is NavigationEvent.LocationB -> {

                            navController.navigate(
                                Screen.Location.createRoute(
                                    type = "B",
                                    latitude =
                                        event.location.latitude,
                                    longitude =
                                        event.location.longitude,
                                    aqi =
                                        event.location.aqi,
                                    name =
                                        event.location.name
                                )
                            )
                        }


                        // -------------------------------------
                        // BOOKING
                        // -------------------------------------

                        is NavigationEvent.Booking -> {
                            navController.navigate(
                                Screen.Booking.route
                            )
                        }


                        // -------------------------------------
                        // HISTORY
                        // -------------------------------------

                        NavigationEvent.History -> {

                            navController.navigate(
                                Screen.History.route
                            )
                        }


                        // -------------------------------------
                        // BACK TO MAP
                        // -------------------------------------

                        NavigationEvent.BackToMap -> {

                            navController.popBackStack()
                        }
                    }
                }
            )
        }


        // =====================================================
        // SCREEN 2 — LOCATION
        // =====================================================

        composable(
            route = Screen.Location.route,

            arguments = listOf(

                navArgument("type") {
                    type = NavType.StringType
                },

                navArgument("latitude") {
                    type = NavType.StringType
                },

                navArgument("longitude") {
                    type = NavType.StringType
                },

                navArgument("aqi") {
                    type = NavType.IntType
                },

                navArgument("name") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            // -------------------------------------------------
            // READ NAVIGATION ARGUMENTS
            // -------------------------------------------------

            val type =
                backStackEntry.arguments
                    ?.getString("type")
                    ?.let(Uri::decode)
                    ?: "A"


            val latitude =
                backStackEntry.arguments
                    ?.getString("latitude")
                    ?.toDoubleOrNull()
                    ?: 0.0


            val longitude =
                backStackEntry.arguments
                    ?.getString("longitude")
                    ?.toDoubleOrNull()
                    ?: 0.0


            val aqi =
                backStackEntry.arguments
                    ?.getInt("aqi")
                    ?: 0


            val name =
                backStackEntry.arguments
                    ?.getString("name")
                    ?.let(Uri::decode)
                    ?: ""


            // -------------------------------------------------
            // LOCATION SCREEN
            // -------------------------------------------------

            LocationScreen(
                type = type,
                latitude = latitude,
                longitude = longitude,
                aqi = aqi,
                name = name,
                onSave = { locationType, nickname ->

                    /*
                     * previousBackStackEntry is Screen 1.
                     *
                     * Store the Screen 2 result there before
                     * returning to Screen 1.
                     */

                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "location_type",
                            locationType
                        )

                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "location_nickname",
                            nickname
                        )

                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // =====================================================
        // SCREEN 3 — BOOKING
        // =====================================================

        composable(
            route = Screen.Booking.route
        ) {

            BookingScreen(

                onHistory = {

                    navController.navigate(
                        Screen.History.route
                    )
                },

                onBackToMap = {

                    mapViewModel.resetMap()

                    navController.navigate(
                        Screen.Map.route
                    ) {

                        popUpTo(
                            Screen.Map.route
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }


        // =====================================================
        // SCREEN 4 — HISTORY
        // =====================================================

        composable(
            route = Screen.History.route
        ) {
            HistoryScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}