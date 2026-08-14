package com.shetty.mapbooking.presentation.screen.booking

import com.shetty.mapbooking.data.model.BookDetails

data class BookingUiState(
    val isLoading: Boolean = false,
    val book: BookDetails? = null,
    val error: String? = null
)