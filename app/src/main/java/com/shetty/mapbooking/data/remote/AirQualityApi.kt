package com.shetty.mapbooking.data.remote

import com.shetty.mapbooking.data.model.AirQualityResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface AirQualityApi {

    @GET("feed/geo:{latitude};{longitude}/")
    suspend fun getAirQuality(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
        @Query("token") token: String
    ): AirQualityResponse
}