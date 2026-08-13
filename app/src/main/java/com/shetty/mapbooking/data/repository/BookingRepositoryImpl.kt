package com.shetty.mapbooking.data.repository

import com.shetty.mapbooking.data.model.BookDetails
import com.shetty.mapbooking.data.model.BookRequest
import com.shetty.mapbooking.data.remote.BookingDataSource
import com.shetty.mapbooking.domain.repository.BookingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepositoryImpl @Inject constructor(
    private val bookingDataSource: BookingDataSource
) : BookingRepository {

    override suspend fun createBook(
        request: BookRequest
    ): Result<BookDetails> {
        return runCatching {
            bookingDataSource.createBook(request)
        }
    }

    override suspend fun getBooks(
        year: Int,
        month: Int
    ): Result<List<BookDetails>> {
        return runCatching {
            bookingDataSource.getBooks(year, month)
        }
    }
}