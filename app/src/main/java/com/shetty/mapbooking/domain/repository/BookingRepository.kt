package com.shetty.mapbooking.domain.repository

import com.shetty.mapbooking.data.model.BookDetails
import com.shetty.mapbooking.data.model.BookRequest

interface BookingRepository {

    suspend fun createBook(
        request: BookRequest
    ): Result<BookDetails>

    suspend fun getBooks(
        year: Int,
        month: Int
    ): Result<List<BookDetails>>
}