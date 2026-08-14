package com.shetty.mapbooking.presentation.screen.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shetty.mapbooking.data.model.BookDetails
import com.shetty.mapbooking.presentation.components.AppHeader


@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {

    // =========================================================
    // STATE
    // =========================================================

    val uiState by viewModel.uiState.collectAsState()


    // =========================================================
    // LOAD CURRENT MONTH HISTORY
    // =========================================================

    LaunchedEffect(Unit) {

        viewModel.loadHistory()
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
            title = "History"
        )


        // =====================================================
        // SUMMARY
        // =====================================================

        HistorySummary(
            totalItems = uiState.totalItems,
            totalPrice = uiState.totalPrice
        )


        // =====================================================
        // DIVIDER
        // =====================================================

        HorizontalDivider()


        // =====================================================
        // LOADING
        // =====================================================

        if (uiState.isLoading) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                CircularProgressIndicator()

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Loading history...",

                    fontSize = 13.sp,

                    color = Color.Gray
                )
            }
        }


        // =====================================================
        // ERROR
        // =====================================================

        uiState.error?.let { error ->

            Text(
                text = error,

                color =
                    MaterialTheme.colorScheme.error,

                modifier = Modifier.padding(16.dp)
            )
        }


        // =====================================================
        // BOOKING LIST
        // =====================================================

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),

            contentPadding =
                androidx.compose.foundation.layout
                    .PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    ),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            items(
                items = uiState.books
            ) { book ->

                HistoryBookingCard(
                    book = book
                )
            }
        }


        // =====================================================
        // BACK BUTTON
        // =====================================================

        Button(
            onClick = onBack,

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
                .height(48.dp)
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


// =============================================================
// HISTORY SUMMARY
// =============================================================

@Composable
private fun HistorySummary(
    totalItems: Int,
    totalPrice: Double
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        // =====================================================
        // TOTAL BOOKINGS
        // =====================================================

        Column {

            Text(
                text = "Total Bookings",

                fontSize = 12.sp,

                fontWeight =
                    FontWeight.Bold,

                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = totalItems.toString(),

                fontSize = 20.sp,

                fontWeight =
                    FontWeight.Bold,

                color = Color.Black
            )
        }


        // =====================================================
        // TOTAL SPENT
        // =====================================================

        Column(
            horizontalAlignment =
                Alignment.End
        ) {

            Text(
                text = "Total Spent",

                fontSize = 12.sp,

                fontWeight =
                    FontWeight.Bold,

                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = formatPrice(totalPrice),

                fontSize = 20.sp,

                fontWeight =
                    FontWeight.Bold,

                color = Color.Black
            )
        }
    }
}


// =============================================================
// HISTORY BOOKING CARD
// =============================================================

@Composable
private fun HistoryBookingCard(
    book: BookDetails
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(10.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme
                    .colorScheme
                    .primary.copy(
                        alpha = 0.15f
                    )
            ),

        border =
            BorderStroke(
                width = 1.5.dp,
                color =
                    MaterialTheme
                        .colorScheme
                        .primary
                        .copy(
                            alpha = 0.75f
                        )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {

            // =================================================
            // SOURCE
            // =================================================

            Text(
                text = "SOURCE",
                fontSize = 11.sp,
                fontWeight =
                    FontWeight.Bold,
                color = Color.Gray
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            Text(
                text = book.a.name,
                fontSize = 15.sp,
                fontWeight =
                    FontWeight.Bold,
                color = Color.Black
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            // =================================================
            // DESTINATION
            // =================================================

            Text(
                text = "DESTINATION",
                fontSize = 11.sp,
                fontWeight =
                    FontWeight.Bold,
                color = Color.Gray
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            Text(
                text = book.b.name,
                fontSize = 15.sp,
                fontWeight =
                    FontWeight.Bold,
                color = Color.Black
            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            // =================================================
            // DIVIDER
            // =================================================

            HorizontalDivider()


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            // =================================================
            // PRICE
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "Price",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Text(
                    text =
                        formatPrice(
                            book.price
                        ),
                    fontSize = 15.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color = Color.Black
                )
            }
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