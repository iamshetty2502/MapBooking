package com.shetty.mapbooking.presentation.state

import com.shetty.mapbooking.data.model.LocationDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingStateHolder @Inject constructor() {

    var aLocation: LocationDetails? = null

    var bLocation: LocationDetails? = null

    fun clear() {
        aLocation = null
        bLocation = null
    }
}