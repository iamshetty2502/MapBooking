package com.shetty.mapbooking.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shetty.mapbooking.domain.usecase.GetBookingHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getBookingHistoryUseCase: GetBookingHistoryUseCase
) : ViewModel() {

    // =========================================================
    // STATE
    // =========================================================

    private val _uiState =
        MutableStateFlow(
            HistoryUiState()
        )

    val uiState =
        _uiState.asStateFlow()


    // =========================================================
    // LOAD HISTORY
    // =========================================================

    fun loadHistory() {

        if (_uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            // -------------------------------------------------
            // CURRENT YEAR + CURRENT MONTH
            // -------------------------------------------------

            val currentDate =
                LocalDate. now()

            val year =
                currentDate.year

            val month =
                currentDate.monthValue


            // -------------------------------------------------
            // GET BOOKING HISTORY
            // -------------------------------------------------

            getBookingHistoryUseCase(
                year = year,
                month = month
            )
                .onSuccess { books ->

                    // -------------------------------------------------
                    // TOTAL NUMBER OF ITEMS
                    // -------------------------------------------------

                    val totalItems =
                        books.size


                    // -------------------------------------------------
                    // TOTAL PRICE
                    // -------------------------------------------------

                    val totalPrice =
                        books.sumOf { book ->
                            book.price
                        }


                    // -------------------------------------------------
                    // UPDATE STATE
                    // -------------------------------------------------

                    _uiState.update {

                        it.copy(
                            isLoading = false,
                            books = books,
                            totalItems = totalItems,
                            totalPrice = totalPrice,
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
                                    ?: "Unable to load booking history"
                        )
                    }
                }
        }
    }


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        _uiState.update {
            it.copy(
                error = null
            )
        }
    }
}