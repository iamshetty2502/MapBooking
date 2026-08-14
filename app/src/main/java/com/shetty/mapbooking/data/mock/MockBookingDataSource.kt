package com.shetty.mapbooking.data.mock

import com.shetty.mapbooking.data.model.BookDetails
import com.shetty.mapbooking.data.model.BookRequest
import com.shetty.mapbooking.data.remote.BookingDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class MockBookingDataSource @Inject constructor() :
    BookingDataSource {

    // =========================================================
    // POST /books
    // =========================================================

    override suspend fun createBook(
        request: BookRequest
    ): BookDetails {

        // Simulate network delay.
        delay(500.milliseconds)

        return BookDetails(
            a = request.a,
            b = request.b,
            price = 10000.0
        )
    }


    // =========================================================
    // GET /books
    // =========================================================

    override suspend fun getBooks(
        year: Int,
        month: Int
    ): List<BookDetails> {

        // Simulate network delay.
        delay(500.milliseconds)

        /*
         * Mock response for the requested month.
         *
         * In the real API this would be:
         *
         * GET /books?year=2026&month=8
         *
         * For now, we return sample bookings.
         */

        return listOf(

            BookDetails(
                a = createLocation(
                    latitude = 36.564,
                    longitude = 127.001,
                    aqi = 30,
                    name = "Seoul A Location"
                ),

                b = createLocation(
                    latitude = 36.567,
                    longitude = 127.000,
                    aqi = 40,
                    name = "Seoul B Location"
                ),

                price = 10000.0
            ),

            BookDetails(
                a = createLocation(
                    latitude = 36.568,
                    longitude = 127.002,
                    aqi = 25,
                    name = "Seoul C Location"
                ),

                b = createLocation(
                    latitude = 36.570,
                    longitude = 127.005,
                    aqi = 35,
                    name = "Seoul D Location"
                ),

                price = 8500.0
            ),

            BookDetails(
                a = createLocation(
                    latitude = 36.571,
                    longitude = 127.008,
                    aqi = 28,
                    name = "Seoul E Location"
                ),

                b = createLocation(
                    latitude = 36.575,
                    longitude = 127.010,
                    aqi = 45,
                    name = "Seoul F Location"
                ),

                price = 12000.0
            )
        )
    }


    // =========================================================
    // MOCK LOCATION
    // =========================================================

    private fun createLocation(
        latitude: Double,
        longitude: Double,
        aqi: Int,
        name: String
    ) = com.shetty.mapbooking.data.model.LocationDetails(
        latitude = latitude,
        longitude = longitude,
        aqi = aqi,
        name = name
    )
}