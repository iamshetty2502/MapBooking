package com.shetty.mapbooking.presentation.screen.location

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shetty.mapbooking.data.model.LocationDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LocationUiState())

    val uiState =
        _uiState.asStateFlow()


    fun initialize(
        location: LocationDetails
    ) {

        if (_uiState.value.location != null) {
            return
        }

        _uiState.update {
            it.copy(
                location = location
            )
        }
    }


    fun onNicknameChanged(
        nickname: String
    ) {

        if (nickname.length > MAX_NICKNAME_LENGTH) {
            return
        }

        _uiState.update {
            it.copy(
                nickname = nickname
            )
        }
    }


    companion object {
        const val MAX_NICKNAME_LENGTH = 20
    }
}