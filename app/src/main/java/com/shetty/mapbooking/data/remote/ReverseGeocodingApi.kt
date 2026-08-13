package com.shetty.mapbooking.data.remote

import com.shetty.mapbooking.data.model.ReverseGeocodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ReverseGeocodingApi {

    @GET("data/reverse-geocode-client")
    suspend fun getLocationInfo(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("localityLanguage") language: String = "en"
    ): ReverseGeocodeResponse
}