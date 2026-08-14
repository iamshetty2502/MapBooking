package com.shetty.mapbooking.presentation.navigation

import com.shetty.mapbooking.data.model.LocationDetails

data class BookingNavigationData(
    val aLocation: LocationDetails,
    val bLocation: LocationDetails
)