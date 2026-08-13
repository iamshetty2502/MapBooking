package com.shetty.mapbooking.presentation.navigation

sealed interface NavigationEvent {

    data object LocationA : NavigationEvent

    data object LocationB : NavigationEvent

    data object Booking : NavigationEvent

    data object History : NavigationEvent

    data object BackToMap : NavigationEvent
}