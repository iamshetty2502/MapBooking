package com.shetty.mapbooking.presentation.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.shetty.mapbooking.data.location.LocationManager
import com.shetty.mapbooking.domain.usecase.GetLocationDetailsUseCase
import com.shetty.mapbooking.presentation.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getLocationDetailsUseCase: GetLocationDetailsUseCase,
    private val locationManager: LocationManager
) : ViewModel() {

    // =========================================================
    // STATE
    // =========================================================

    private val _uiState =
        MutableStateFlow(MapUiState())

    val uiState =
        _uiState.asStateFlow()


    // =========================================================
    // NAVIGATION
    // =========================================================

    private val _navigationEvent =
        MutableSharedFlow<NavigationEvent>()

    val navigationEvent =
        _navigationEvent.asSharedFlow()


    // =========================================================
    // INITIAL USER LOCATION
    // =========================================================

    fun loadCurrentLocation() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoadingLocation = true,
                    error = null
                )
            }

            val location =
                locationManager.getCurrentLocation()

            if (location != null) {

                val latLng = LatLng(
                    location.latitude,
                    location.longitude
                )

                _uiState.update {
                    it.copy(
                        currentLocation = latLng,
                        selectedLocation = latLng,
                        isLoadingLocation = false,
                        error = null
                    )
                }

            } else {

                _uiState.update {
                    it.copy(
                        isLoadingLocation = false,
                        error = "Unable to get current location"
                    )
                }
            }
        }
    }


    // =========================================================
    // SELECTED LOCATION
    // =========================================================

    fun onSelectedLocationChanged(
        latitude: Double,
        longitude: Double
    ) {

        _uiState.update {
            it.copy(
                selectedLocation = LatLng(
                    latitude,
                    longitude
                )
            )
        }
    }


    // =========================================================
    // USER ACTION
    // =========================================================

    fun onAction(action: MapAction) {

        when (action) {

            MapAction.SetA -> {
                setAFromSelectedLocation()
            }

            MapAction.SetB -> {
                setBFromSelectedLocation()
            }

            MapAction.Book -> {
                onBookClicked()
            }
        }
    }


    // =========================================================
    // SET A
    // =========================================================

    private fun setAFromSelectedLocation() {

        val selectedLocation =
            _uiState.value.selectedLocation
                ?: return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoadingLocation = true,
                    error = null
                )
            }

            getLocationDetailsUseCase(
                latitude = selectedLocation.latitude,
                longitude = selectedLocation.longitude
            )
                .onSuccess { locationDetails ->

                    _uiState.update {
                        it.copy(
                            aLocation = locationDetails,
                            isLoadingLocation = false,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->

                    _uiState.update {
                        it.copy(
                            isLoadingLocation = false,
                            error = throwable.message
                                ?: "Unable to set A location"
                        )
                    }
                }
        }
    }


    // =========================================================
    // SET B
    // =========================================================

    private fun setBFromSelectedLocation() {

        val selectedLocation =
            _uiState.value.selectedLocation
                ?: return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoadingLocation = true,
                    error = null
                )
            }

            getLocationDetailsUseCase(
                latitude = selectedLocation.latitude,
                longitude = selectedLocation.longitude
            )
                .onSuccess { locationDetails ->

                    _uiState.update {
                        it.copy(
                            bLocation = locationDetails,
                            isLoadingLocation = false,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->

                    _uiState.update {
                        it.copy(
                            isLoadingLocation = false,
                            error = throwable.message
                                ?: "Unable to set B location"
                        )
                    }
                }
        }
    }


    // =========================================================
    // NAVIGATION
    // =========================================================

    private fun onBookClicked() {

        viewModelScope.launch {

            _navigationEvent.emit(
                NavigationEvent.Booking
            )
        }
    }


    fun loadSelectedLocationDetails() {

        val selectedLocation =
            _uiState.value.selectedLocation
                ?: return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoadingLocation = true,
                    error = null
                )
            }

            getLocationDetailsUseCase(
                latitude = selectedLocation.latitude,
                longitude = selectedLocation.longitude
            )
                .onSuccess { locationDetails ->

                    _uiState.update {
                        it.copy(
                            selectedLocationDetails =
                                locationDetails,
                            isLoadingLocation = false,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->

                    _uiState.update {
                        it.copy(
                            isLoadingLocation = false,
                            error = throwable.message
                                ?: "Unable to load location details"
                        )
                    }
                }
        }
    }

    fun onHistoryClicked() {

        viewModelScope.launch {

            _navigationEvent.emit(
                NavigationEvent.History
            )
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