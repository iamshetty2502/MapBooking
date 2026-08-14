package com.shetty.mapbooking.presentation.screen.history

import com.shetty.mapbooking.data.model.BookDetails

data class HistoryUiState(
    val isLoading: Boolean = false,
    val books: List<BookDetails> = emptyList(),
    val totalItems: Int = 0,
    val totalPrice: Double = 0.0,
    val error: String? = null
)