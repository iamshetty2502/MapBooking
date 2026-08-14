package com.shetty.mapbooking.domain.usecase

import com.shetty.mapbooking.data.model.BookDetails
import com.shetty.mapbooking.domain.repository.BookingRepository
import javax.inject.Inject

class GetBookingHistoryUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {

    suspend operator fun invoke(
        year: Int,
        month: Int
    ): Result<List<BookDetails>> {
        return bookingRepository.getBooks(
            year = year,
            month = month
        )
    }
}