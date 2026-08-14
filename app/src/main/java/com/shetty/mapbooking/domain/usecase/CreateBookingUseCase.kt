package com.shetty.mapbooking.domain.usecase

import com.shetty.mapbooking.data.model.BookDetails
import com.shetty.mapbooking.data.model.BookRequest
import com.shetty.mapbooking.domain.repository.BookingRepository
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {

    suspend operator fun invoke(
        request: BookRequest
    ): Result<BookDetails> {
        return bookingRepository.createBook(request)
    }
}