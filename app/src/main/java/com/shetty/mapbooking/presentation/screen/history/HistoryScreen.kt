package com.shetty.mapbooking.presentation.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.shetty.mapbooking.data.model.BookDetails

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {

    // =========================================================
    // STATE
    // =========================================================

    val uiState by
    viewModel.uiState.collectAsState()


    // =========================================================
    // LOAD HISTORY
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
            .padding(16.dp)
    ) {

        // =====================================================
        // TITLE
        // =====================================================

        Text(
            text = "Usage History",
            style = MaterialTheme.typography.headlineMedium
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // =====================================================
        // SUMMARY
        // =====================================================

        Text(
            text = "Total bookings: ${uiState.totalItems}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Total price: ₹${uiState.totalPrice}",
            style = MaterialTheme.typography.titleMedium
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // =====================================================
        // LOADING
        // =====================================================

        if (uiState.isLoading) {

            CircularProgressIndicator()

            Spacer(
                modifier = Modifier.height(16.dp)
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
                modifier = Modifier.height(8.dp)
            )

            Button(
                onClick = {

                    viewModel.clearError()
                    viewModel.loadHistory()
                }
            ) {

                Text(
                    text = "Retry"
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }


        // =====================================================
        // HISTORY LIST
        // =====================================================

        LazyColumn(
            modifier = Modifier.weight(1f),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            items(
                items = uiState.books
            ) { book ->

                HistoryItem(
                    book = book
                )
            }
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // =====================================================
        // BACK
        // =====================================================

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Back"
            )
        }
    }
}


// =============================================================
// HISTORY ITEM
// =============================================================

@Composable
private fun HistoryItem(
    book: BookDetails
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // =================================================
            // LOCATION A
            // =================================================

            Text(
                text = "Location A",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = book.a.name
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text =
                    "Latitude: %.6f"
                        .format(book.a.latitude)
            )

            Text(
                text =
                    "Longitude: %.6f"
                        .format(book.a.longitude)
            )

            Text(
                text = "AQI: ${book.a.aqi}"
            )


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // =================================================
            // LOCATION B
            // =================================================

            Text(
                text = "Location B",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = book.b.name
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text =
                    "Latitude: %.6f"
                        .format(book.b.latitude)
            )

            Text(
                text =
                    "Longitude: %.6f"
                        .format(book.b.longitude)
            )

            Text(
                text = "AQI: ${book.b.aqi}"
            )


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // =================================================
            // PRICE
            // =================================================

            Text(
                text = "Price: ₹${book.price}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}