package com.shetty.mapbooking.presentation.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
            .padding(
                bottom = 12.dp
            )
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                ),

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            // -------------------------------------------------
            // TOTAL COUNT
            // -------------------------------------------------

            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Total Count",

                    fontSize = 12.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text =
                        uiState.totalItems.toString(),

                    fontSize = 16.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            // -------------------------------------------------
            // TOTAL PRICE
            // -------------------------------------------------

            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Total Price",

                    fontSize = 12.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text =
                        formatPrice(
                            uiState.totalPrice
                        ),

                    fontSize = 16.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        // =====================================================
        // DIVIDER
        // =====================================================

        HorizontalDivider()


        // =====================================================
        // LOADING
        // =====================================================

        if (uiState.isLoading) {

            CircularProgressIndicator(
                modifier = Modifier
                    .align(
                        Alignment.CenterHorizontally
                    )
                    .padding(16.dp)
            )
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

            verticalArrangement =
                Arrangement.Top
        ) {

            items(
                items = uiState.books
            ) { book ->

                HistoryItem(
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
                    horizontal = 16.dp
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
// HISTORY ITEM
// =============================================================

@Composable
private fun HistoryItem(
    book: BookDetails
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // =====================================================
        // LOCATION A
        // =====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
        ) {

            Text(
                text = "A",
                modifier = Modifier.width(
                    32.dp
                ),
                fontSize = 13.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = book.a.name,
                fontSize = 13.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }


        // =====================================================
        // LOCATION B
        // =====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
        ) {

            Text(
                text = "B",
                modifier = Modifier.width(
                    32.dp
                ),
                fontSize = 13.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = book.b.name,

                fontSize = 13.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }


        // =====================================================
        // DIVIDER
        // =====================================================

        HorizontalDivider()
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