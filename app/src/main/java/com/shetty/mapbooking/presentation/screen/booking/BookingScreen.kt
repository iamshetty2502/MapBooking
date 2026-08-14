package com.shetty.mapbooking.presentation.screen.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shetty.mapbooking.data.model.BookDetails
import com.shetty.mapbooking.presentation.components.AppHeader
import com.shetty.mapbooking.presentation.components.ConfirmationDialog


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

    var showBackConfirmation by remember {
        mutableStateOf(false)
    }


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
            .navigationBarsPadding()
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        AppHeader(
            title = "Booking"
        )


        // =====================================================
        // CONTENT
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 20.dp,
                    bottom = 12.dp
                )
        ) {

            // =================================================
            // LOADING
            // =================================================

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


            // =================================================
            // ERROR
            // =================================================

            uiState.error?.let { error ->

                Text(
                    text = error,

                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    onClick = {

                        viewModel.clearError()

                        viewModel.createBooking()
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Retry"
                    )
                }
            }


            // =================================================
            // BOOKING RESPONSE
            // =================================================

            uiState.book?.let { book ->

                BookingDetailsContent(
                    book = book
                )
            }


            // =================================================
            // PUSH BUTTONS TO BOTTOM
            // =================================================

            Spacer(
                modifier = Modifier.weight(1f)
            )


            // =================================================
            // VIEW HISTORY
            // =================================================

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
                    fontSize = 14.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // =================================================
            // BACK
            // =================================================

            Button(
                onClick = {

                    showBackConfirmation = true
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !uiState.isLoading
            ) {

                Text(
                    text = "Back",
                    fontSize = 14.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }


    // =========================================================
    // BACK CONFIRMATION
    // =========================================================

    if (showBackConfirmation) {
        ConfirmationDialog(
            title = "Go Back?",
            message =
                "If you go back, the booking information will be reset. Do you want to continue?",
            confirmText = "Yes",
            dismissText = "No",
            onConfirm = {
                showBackConfirmation = false
                onBackToMap()
            },

            onDismiss = {
                showBackConfirmation = false
            }
        )
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
        // SOURCE
        // =====================================================

        BookingLocationCard(
            label = "Source",
            name = book.a.name,
            aqi = book.a.aqi,
            nickname = book.a.nickname
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // =====================================================
        // DESTINATION
        // =====================================================

        BookingLocationCard(
            label = "Destination",
            name = book.b.name,
            aqi = book.b.aqi,
            nickname = book.b.nickname
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // =====================================================
        // PRICE
        // =====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 4.dp
                ),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Price",
                fontSize = 16.sp,
                fontWeight =
                    FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text =
                    "${formatPrice(book.price)} /-",
                fontSize = 18.sp,
                fontWeight =
                    FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}


// =============================================================
// LOCATION CARD
// =============================================================

@Composable
private fun BookingLocationCard(
    label: String,
    name: String,
    aqi: Int,
    nickname: String?
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                            .copy(
                                alpha = 0.35f
                            )
                ),

                shape =
                    RoundedCornerShape(
                        10.dp
                    )
            )
            .padding(14.dp)
    ) {

        // =====================================================
        // SOURCE / DESTINATION
        // =====================================================

        Text(
            text = label.uppercase(),
            fontSize = 13.sp,
            fontWeight =
                FontWeight.Bold,
            color = Color.Black
        )


        Spacer(
            modifier = Modifier.height(4.dp)
        )


        // =====================================================
        // LOCATION NAME
        // =====================================================

        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight =
                FontWeight.Bold,
            color = Color.Black
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // =====================================================
        // AQI
        // =====================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = "AQI",
                fontSize = 13.sp,
                fontWeight =
                    FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = aqi.toString(),
                fontSize = 13.sp,
                fontWeight =
                    FontWeight.Bold,
                color = Color.Gray
            )
        }


        Spacer(
            modifier = Modifier.height(6.dp)
        )


        // =====================================================
        // NICKNAME
        // =====================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = "Nickname",
                fontSize = 13.sp,
                fontWeight =
                    FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text =
                    if (nickname.isNullOrBlank()) {
                        "Optional"
                    } else {
                        nickname
                    },
                fontSize = 13.sp,
                fontWeight =
                    FontWeight.Bold,
                color =  if (nickname.isNullOrBlank()) {
                    Color.LightGray
                } else {
                    Color.DarkGray
                }
            )
        }
    }
}


// =============================================================
// PRICE FORMAT
// =============================================================

private fun formatPrice(
    price: Double
): String {

    return if (price % 1.0 == 0.0) {

        price
            .toLong()
            .toString()

    } else {

        price.toString()
    }
}