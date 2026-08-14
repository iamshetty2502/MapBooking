package com.shetty.mapbooking.presentation.screen.map

sealed interface MapAction {

    data object SetA : MapAction

    data object SetB : MapAction

    data object Book : MapAction
}