package com.shetty.mapbooking.presentation.screen.map

import com.shetty.mapbooking.data.model.LocationDetails

data class MapUiState(
    val currentLocation: LocationDetails? = null,
    val aLocation: LocationDetails? = null,
    val bLocation: LocationDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)