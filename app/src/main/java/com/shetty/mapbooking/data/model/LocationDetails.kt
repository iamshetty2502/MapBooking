package com.shetty.mapbooking.data.model


data class LocationDetails(
    val latitude: Double,
    val longitude: Double,
    val aqi: Int,
    val name: String,
    val nickname: String? = null
) {

    val displayName: String
        get() = nickname
            ?.takeIf { it.isNotBlank() }
            ?: name
}