package com.shetty.mapbooking.presentation.screen.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shetty.mapbooking.data.model.BookDetails
import com.shetty.mapbooking.presentation.components.AppHeader


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

    LaunchedEffect(Unit) {
        viewModel.createBooking()
    }


    // =========================================================
    // SCREEN
    // =========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 12.dp
            )
    ) {

        AppHeader(
            title = "Booking"
        )

        // =====================================================
        // LOADING
        // =====================================================

        if (uiState.isLoading) {

            CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )

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
                color = MaterialTheme.colorScheme.error
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Button(
                onClick = {
                    viewModel.clearError()
                    viewModel.createBooking()
                },

                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Retry")
            }
        }


        // =====================================================
        // BOOKING RESPONSE
        // =====================================================

        uiState.book?.let { book ->

            BookingDetailsContent(
                book = book
            )
        }


        // =====================================================
        // PUSH BUTTONS TO BOTTOM
        // =====================================================

        Spacer(
            modifier = Modifier.weight(1f)
        )


        // =====================================================
        // VIEW HISTORY
        // =====================================================

        Button(
            onClick = onHistory,

            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),

            enabled =
                !uiState.isLoading &&
                        uiState.book != null
        ) {

            Text(
                text = "View History",

                fontSize = 14.sp
            )
        }


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        // =====================================================
        // BACK
        // =====================================================

        Button(
            onClick = onBackToMap,

            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {

            Text(
                text = "BACK",

                fontSize = 14.sp
            )
        }
    }
}


// =============================================================
// BOOKING DETAILS
// =============================================================

@Composable
private fun BookingDetailsContent(
    book: BookDetails
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // =====================================================
        // LOCATION A
        // =====================================================

        BookingLocationRow(
            label = "A",
            name = book.a.name,
            aqi = book.a.aqi,
            nickname = book.a.nickname ?: ""
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        HorizontalDivider()


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        // =====================================================
        // LOCATION B
        // =====================================================

        BookingLocationRow(
            label = "B",
            name = book.b.name,
            aqi = book.b.aqi,
            nickname = book.b.nickname ?: ""
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        HorizontalDivider()


        // =====================================================
        // PRICE
        // =====================================================

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "price",

                fontSize = 14.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = book.price.toString(),

                fontSize = 16.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// =============================================================
// LOCATION ROW
// =============================================================

@Composable
private fun BookingLocationRow(
    label: String,
    name: String,
    aqi: Int,
    nickname: String
) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        // =====================================================
        // A / B
        // =====================================================

        Text(
            text = label,

            modifier = Modifier.width(
                28.dp
            ),

            fontSize = 16.sp,

            fontWeight =
                FontWeight.Bold
        )


        // =====================================================
        // LOCATION INFORMATION
        // =====================================================

        Column(
            modifier = Modifier.weight(1f)
        ) {

            // -------------------------------------------------
            // LOCATION NAME
            // -------------------------------------------------

            Text(
                text = name,

                fontSize = 15.sp,

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // -------------------------------------------------
            // AQI
            // -------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "aqi",

                    fontSize = 12.sp,

                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = aqi.toString(),

                    fontSize = 12.sp,

                    fontWeight =
                        FontWeight.Bold,

                    modifier = Modifier.weight(0.55f)
                )
            }


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            // -------------------------------------------------
            // NICKNAME
            // -------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "nickname",

                    fontSize = 12.sp,

                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = nickname,

                    fontSize = 12.sp,

                    fontWeight =
                        FontWeight.Bold,

                    modifier = Modifier.weight(0.55f)
                )
            }
        }
    }
}