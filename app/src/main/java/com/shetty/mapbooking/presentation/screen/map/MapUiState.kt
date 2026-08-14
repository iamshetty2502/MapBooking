package com.shetty.mapbooking.presentation.screen.map

import com.google.android.gms.maps.model.LatLng
import com.shetty.mapbooking.data.model.LocationDetails

data class MapUiState(

    val currentLocation: LatLng? = null,

    val selectedLocation: LatLng? = null,

    val selectedLocationDetails: LocationDetails? = null,

    val aLocation: LocationDetails? = null,

    val bLocation: LocationDetails? = null,

    val isLoadingLocation: Boolean = false,

    val error: String? = null
) {

    val buttonText: String
        get() = when {
            aLocation == null -> "Set A"
            bLocation == null -> "Set B"
            else -> "Book"
        }

    val canSetA: Boolean
        get() = aLocation == null

    val canSetB: Boolean
        get() = aLocation != null && bLocation == null

    val canBook: Boolean
        get() = aLocation != null && bLocation != null
}