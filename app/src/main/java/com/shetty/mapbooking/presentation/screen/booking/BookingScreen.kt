package com.shetty.mapbooking.presentation.screen.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BookingScreen(
    onHistory: () -> Unit,
    onBackToMap: () -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {

    // =========================================================
    // STATE
    // =========================================================

    val uiState by viewModel.uiState.collectAsState()


    // =========================================================
    // CREATE BOOKING
    // =========================================================

    /*
     * Screen 3 is opened only after A and B have been selected.
     *
     * When Screen 3 enters composition, we call:
     *
     * BookingViewModel
     *       ↓
     * CreateBookUseCase
     *       ↓
     * BookingRepository
     *       ↓
     * MockBookingDataSource
     *       ↓
     * BookDetails
     */

    LaunchedEffect(Unit) {

        viewModel.createBooking()
    }


    // =========================================================
    // SCREEN
    // =========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement =
            Arrangement.Top
    ) {

        // =====================================================
        // TITLE
        // =====================================================

        Text(
            text = "Booking",

            style =
                MaterialTheme.typography.headlineMedium
        )


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // =====================================================
        // LOADING
        // =====================================================

        if (uiState.isLoading) {

            CircularProgressIndicator()

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Creating booking..."
            )
        }


        // =====================================================
        // ERROR
        // =====================================================

        uiState.error?.let { error ->

            Text(
                text = error,

                color =
                    MaterialTheme.colorScheme.error
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = {

                    viewModel.clearError()

                    viewModel.createBooking()
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Retry"
                )
            }
        }


        // =====================================================
        // BOOKING RESPONSE
        // =====================================================

        uiState.book?.let { book ->

            // =================================================
            // LOCATION A
            // =================================================

            Text(
                text = "Location A",

                style =
                    MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            LocationDetailsSection(
                name = book.a.displayName,
                latitude = book.a.latitude,
                longitude = book.a.longitude,
                aqi = book.a.aqi
            )


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            // =================================================
            // LOCATION B
            // =================================================

            Text(
                text = "Location B",

                style =
                    MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            LocationDetailsSection(
                name = book.b.displayName,
                latitude = book.b.latitude,
                longitude = book.b.longitude,
                aqi = book.b.aqi
            )


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            // =================================================
            // PRICE
            // =================================================

            Text(
                text = "Price",

                style =
                    MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "₹${book.price}",

                style =
                    MaterialTheme.typography.headlineSmall
            )
        }


        // Push navigation buttons to bottom.
        Spacer(
            modifier = Modifier.weight(1f)
        )


        // =====================================================
        // VIEW HISTORY
        // =====================================================

        Button(
            onClick = onHistory,

            modifier = Modifier.fillMaxWidth(),

            enabled =
                !uiState.isLoading &&
                        uiState.book != null
        ) {

            Text(
                text = "View History"
            )
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // =====================================================
        // BACK TO MAP
        // =====================================================

        Button(
            onClick = onBackToMap,

            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Back to Map"
            )
        }
    }
}


// =============================================================
// LOCATION DETAILS
// =============================================================

@Composable
private fun LocationDetailsSection(
    name: String,
    latitude: Double,
    longitude: Double,
    aqi: Int
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // -----------------------------------------------------
        // NAME
        // -----------------------------------------------------

        Text(
            text = name,

            style =
                MaterialTheme.typography.bodyLarge
        )


        Spacer(
            modifier = Modifier.height(6.dp)
        )


        // -----------------------------------------------------
        // LATITUDE
        // -----------------------------------------------------

        Text(
            text =
                "Latitude: %.6f"
                    .format(latitude)
        )


        Spacer(
            modifier = Modifier.height(4.dp)
        )


        // -----------------------------------------------------
        // LONGITUDE
        // -----------------------------------------------------

        Text(
            text =
                "Longitude: %.6f"
                    .format(longitude)
        )


        Spacer(
            modifier = Modifier.height(4.dp)
        )


        // -----------------------------------------------------
        // AQI
        // -----------------------------------------------------

        Text(
            text = "AQI: $aqi"
        )
    }
}