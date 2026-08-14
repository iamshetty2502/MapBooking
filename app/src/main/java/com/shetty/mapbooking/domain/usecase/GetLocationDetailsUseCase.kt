package com.shetty.mapbooking.domain.usecase

import com.shetty.mapbooking.data.model.LocationDetails
import com.shetty.mapbooking.domain.repository.LocationRepository
import javax.inject.Inject

class GetLocationDetailsUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): Result<LocationDetails> {
        return locationRepository.getLocationDetails(
            latitude = latitude,
            longitude = longitude
        )
    }
}