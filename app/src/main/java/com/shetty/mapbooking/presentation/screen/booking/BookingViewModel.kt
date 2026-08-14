package com.shetty.mapbooking.presentation.screen.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shetty.mapbooking.data.model.BookRequest
import com.shetty.mapbooking.domain.usecase.CreateBookingUseCase
import com.shetty.mapbooking.presentation.state.BookingStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val createBookingUseCase: CreateBookingUseCase,
    private val bookingStateHolder: BookingStateHolder
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(BookingUiState())

    val uiState =
        _uiState.asStateFlow()


    // =========================================================
    // CREATE BOOKING
    // =========================================================

    fun createBooking() {

        if (_uiState.value.isLoading) {
            return
        }

        val aLocation =
            bookingStateHolder.aLocation

        val bLocation =
            bookingStateHolder.bLocation

        // Both locations are mandatory.
        if (aLocation == null || bLocation == null) {

            _uiState.update {
                it.copy(
                    error = "Both A and B locations are required"
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            val request =
                BookRequest(
                    a = aLocation,
                    b = bLocation
                )

            createBookingUseCase(request)
                .onSuccess { bookDetails ->

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            book = bookDetails,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error =
                                throwable.message
                                    ?: "Unable to create booking"
                        )
                    }
                }
        }
    }


    // =========================================================
    // ERROR
    // =========================================================

    fun clearError() {

        _uiState.update {
            it.copy(
                error = null
            )
        }
    }
}