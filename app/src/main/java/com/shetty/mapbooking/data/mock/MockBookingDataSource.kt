package com.shetty.mapbooking.data.mock

import com.shetty.mapbooking.data.model.BookDetails
import com.shetty.mapbooking.data.model.BookRequest
import com.shetty.mapbooking.data.remote.BookingDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class MockBookingDataSource @Inject constructor() : BookingDataSource {

    private val books = mutableListOf<BookDetails>()

    override suspend fun createBook(
        request: BookRequest
    ): BookDetails {

        // Simulate network delay
        delay(500.milliseconds)

        val book = BookDetails(
            a = request.a,
            b = request.b,
            price = 10000.0
        )

        books.add(book)

        return book
    }

    override suspend fun getBooks(
        year: Int,
        month: Int
    ): List<BookDetails> {

        // Simulate network delay
        delay(500.milliseconds)

        return books.toList()
    }
}