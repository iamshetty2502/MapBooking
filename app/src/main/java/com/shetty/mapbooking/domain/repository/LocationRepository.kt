package com.shetty.mapbooking.domain.repository

import com.shetty.mapbooking.data.model.LocationDetails

interface LocationRepository {

    suspend fun getLocationDetails(
        latitude: Double,
        longitude: Double
    ): Result<LocationDetails>
}