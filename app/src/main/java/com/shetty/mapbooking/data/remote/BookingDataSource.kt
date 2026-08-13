package com.shetty.mapbooking.data.remote

import com.shetty.mapbooking.data.model.BookDetails
import com.shetty.mapbooking.data.model.BookRequest

interface BookingDataSource {

    suspend fun createBook(
        request: BookRequest
    ): BookDetails

    suspend fun getBooks(
        year: Int,
        month: Int
    ): List<BookDetails>
}