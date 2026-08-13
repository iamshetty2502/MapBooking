package com.shetty.mapbooking.data.model

data class LocationDetails(
    val latitude: Double,
    val longitude: Double,
    val aqi: Int,
    val name: String
)