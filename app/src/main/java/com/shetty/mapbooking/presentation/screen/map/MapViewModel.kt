package com.shetty.mapbooking.presentation.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shetty.mapbooking.presentation.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {

    private val _navigationEvent =
        MutableSharedFlow<NavigationEvent>()

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
}