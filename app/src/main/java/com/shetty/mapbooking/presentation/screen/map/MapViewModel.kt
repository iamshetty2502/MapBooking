package com.shetty.mapbooking.presentation.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.shetty.mapbooking.data.location.LocationManager
import com.shetty.mapbooking.domain.usecase.GetLocationDetailsUseCase
import com.shetty.mapbooking.presentation.navigation.NavigationEvent
import com.shetty.mapbooking.presentation.state.BookingStateHolder
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
    private val locationManager: LocationManager,
    private val bookingStateHolder: BookingStateHolder
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

    fun onAction(
        action: MapAction
    ) {

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
                            error =
                                throwable.message
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
                            error =
                                throwable.message
                                    ?: "Unable to set B location"
                        )
                    }
                }
        }
    }


    // =========================================================
    // BOOK
    // =========================================================

    /*
     * Screen 1 -> Screen 3
     *
     * We require both A and B before allowing the booking
     * flow to continue.
     *
     * The actual booking request is NOT made here.
     *
     * We only transfer the selected A/B locations to the
     * BookingStateHolder.
     *
     * BookingViewModel will perform the actual createBook()
     * operation.
     */

    private fun onBookClicked() {

        val state =
            _uiState.value

        val aLocation =
            state.aLocation
                ?: return

        val bLocation =
            state.bLocation
                ?: return

        // Store A and B for Screen 3.
        bookingStateHolder.aLocation =
            aLocation

        bookingStateHolder.bLocation =
            bLocation

        viewModelScope.launch {

            _navigationEvent.emit(
                NavigationEvent.Booking
            )
        }
    }


    // =========================================================
    // SELECTED LOCATION DETAILS
    // =========================================================

    /*
     * Called after the map stops moving.
     *
     * Coordinates
     *      ↓
     * GetLocationDetailsUseCase
     *      ↓
     * AQI + Reverse Geocoding
     *      ↓
     * selectedLocationDetails
     */

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
                            error =
                                throwable.message
                                    ?: "Unable to load location details"
                        )
                    }
                }
        }
    }


    // =========================================================
    // LOCATION A LABEL CLICK
    // =========================================================

    /*
     * Tapping the existing A label opens Screen 2.
     */

    fun onLocationAClicked() {

        val location =
            _uiState.value.aLocation
                ?: return

        viewModelScope.launch {

            _navigationEvent.emit(
                NavigationEvent.LocationA(
                    location = location
                )
            )
        }
    }


    // =========================================================
    // LOCATION B LABEL CLICK
    // =========================================================

    /*
     * Tapping the existing B label opens Screen 2.
     */

    fun onLocationBClicked() {

        val location =
            _uiState.value.bLocation
                ?: return

        viewModelScope.launch {

            _navigationEvent.emit(
                NavigationEvent.LocationB(
                    location = location
                )
            )
        }
    }


    // =========================================================
    // UPDATE NICKNAME
    // =========================================================

    /*
     * Screen 2 -> Screen 1
     *
     * We preserve the original reverse-geocoded name.
     * The nickname is stored separately.
     *
     * Example:
     *
     * name     = "Seocho District, Yangjae 2(i)-dong"
     * nickname = "Home"
     *
     * MapScreen displays:
     *
     * location.displayName
     */

    fun updateNickname(
        type: String,
        nickname: String
    ) {

        val trimmedNickname =
            nickname.trim()

        _uiState.update { state ->

            when (type) {

                "A" -> {

                    val location =
                        state.aLocation
                            ?: return@update state

                    state.copy(
                        aLocation = location.copy(
                            nickname =
                                trimmedNickname
                                    .takeIf {
                                        it.isNotBlank()
                                    }
                        )
                    )
                }

                "B" -> {

                    val location =
                        state.bLocation
                            ?: return@update state

                    state.copy(
                        bLocation = location.copy(
                            nickname =
                                trimmedNickname
                                    .takeIf {
                                        it.isNotBlank()
                                    }
                        )
                    )
                }

                else -> state
            }
        }
    }


    // =========================================================
    // HISTORY
    // =========================================================
    fun onHistoryClicked() {

        viewModelScope.launch {

            _navigationEvent.emit(
                NavigationEvent.History
            )
        }
    }

    // =========================================================
    // RESET MAP
    // =========================================================
    fun resetMap() {

        // Clear A/B booking data
        bookingStateHolder.clear()

        // Reset the Map screen state
        _uiState.value = MapUiState()
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