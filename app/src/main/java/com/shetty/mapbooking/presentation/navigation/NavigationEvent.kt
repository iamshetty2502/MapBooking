package com.shetty.mapbooking.presentation.navigation

import com.shetty.mapbooking.data.model.LocationDetails

sealed interface NavigationEvent {

    data class LocationA(
        val location: LocationDetails
    ) : NavigationEvent

    data class LocationB(
        val location: LocationDetails
    ) : NavigationEvent

    data object Booking : NavigationEvent

    data object History : NavigationEvent

    data object BackToMap : NavigationEvent
}