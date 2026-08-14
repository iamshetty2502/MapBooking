package com.shetty.mapbooking.presentation.screen.location

import com.shetty.mapbooking.data.model.LocationDetails

data class LocationUiState(
    val location: LocationDetails? = null,
    val nickname: String = "",
    val isSaving: Boolean = false,
    val error: String? = null
)