package com.shetty.mapbooking.data.repository

import com.shetty.mapbooking.data.model.LocationDetails
import com.shetty.mapbooking.data.remote.AirQualityApi
import com.shetty.mapbooking.data.remote.ReverseGeocodingApi
import com.shetty.mapbooking.domain.repository.LocationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val airQualityApi: AirQualityApi,
    private val reverseGeocodingApi: ReverseGeocodingApi
) : LocationRepository {

    override suspend fun getLocationDetails(
        latitude: Double,
        longitude: Double
    ): Result<LocationDetails> {

        return runCatching {

            val airQualityResponse =
                airQualityApi.getAirQuality(
                    latitude = latitude,
                    longitude = longitude,
                    token = "TEMP_TOKEN"
                )

            val reverseGeocodeResponse =
                reverseGeocodingApi.getLocationInfo(
                    latitude = latitude,
                    longitude = longitude
                )

            val name = reverseGeocodeResponse
                .localityInfo
                .administrative
                .sortedByDescending { it.order }
                .take(2)
                .joinToString(", ") { it.name }

            LocationDetails(
                latitude = latitude,
                longitude = longitude,
                aqi = airQualityResponse.data.aqi,
                name = name
            )
        }
    }
}