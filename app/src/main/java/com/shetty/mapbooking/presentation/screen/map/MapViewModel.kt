package com.shetty.mapbooking.presentation.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shetty.mapbooking.data.model.LocationDetails
import com.shetty.mapbooking.domain.usecase.GetLocationDetailsUseCase
import com.shetty.mapbooking.presentation.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getLocationDetailsUseCase: GetLocationDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent =
        MutableSharedFlow<NavigationEvent>(
            extraBufferCapacity = 1
        )

    val navigationEvent =
        _navigationEvent.asSharedFlow()

    fun onLocationAClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                NavigationEvent.LocationA
            )
        }
    }

    fun onLocationBClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                NavigationEvent.LocationB
            )
        }
    }

    fun onBookClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                NavigationEvent.Booking
            )
        }
    }

    fun onHistoryClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                NavigationEvent.History
            )
        }
    }

    fun loadLocationDetails(
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            getLocationDetailsUseCase(
                latitude = latitude,
                longitude = longitude
            ).onSuccess { locationDetails ->

                _uiState.value = _uiState.value.copy(
                    currentLocation = locationDetails,
                    isLoading = false
                )

            }.onFailure { throwable ->

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = throwable.message
                        ?: "Unable to load location"
                )
            }
        }
    }
}