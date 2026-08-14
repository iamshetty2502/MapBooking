package com.shetty.mapbooking.presentation.screen.location

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shetty.mapbooking.data.model.LocationDetails

@Composable
fun LocationScreen(
    type: String,
    latitude: Double,
    longitude: Double,
    aqi: Int,
    name: String,
    onSave: (
        type: String,
        nickname: String
    ) -> Unit,
    onBack: () -> Unit,
    viewModel: LocationViewModel =
        hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()


    // ---------------------------------------------------------
    // INITIALIZE LOCATION
    // ---------------------------------------------------------

    LaunchedEffect(
        latitude,
        longitude,
        aqi,
        name
    ) {

        viewModel.initialize(
            LocationDetails(
                latitude = latitude,
                longitude = longitude,
                aqi = aqi,
                name = name
            )
        )
    }


    // ---------------------------------------------------------
    // UI
    // ---------------------------------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        Text(
            text = "Location $type"
        )


        // -----------------------------------------------------
        // ADDRESS
        // -----------------------------------------------------

        Text(
            text = uiState.location?.name
                ?: name
        )


        // -----------------------------------------------------
        // LATITUDE
        // -----------------------------------------------------

        Text(
            text = "Latitude: %.6f".format(
                uiState.location?.latitude
                    ?: latitude
            )
        )


        // -----------------------------------------------------
        // LONGITUDE
        // -----------------------------------------------------

        Text(
            text = "Longitude: %.6f".format(
                uiState.location?.longitude
                    ?: longitude
            )
        )


        // -----------------------------------------------------
        // AQI
        // -----------------------------------------------------

        Text(
            text = "AQI: ${
                uiState.location?.aqi ?: aqi
            }"
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        // -----------------------------------------------------
        // NICKNAME
        // -----------------------------------------------------

        OutlinedTextField(
            value = uiState.nickname,

            onValueChange = {
                viewModel.onNicknameChanged(it)
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Nickname")
            },

            placeholder = {
                Text("Optional")
            },

            singleLine = true,

            supportingText = {
                Text(
                    "${uiState.nickname.length}/20"
                )
            },

            keyboardOptions = KeyboardOptions(
                keyboardType =
                    KeyboardType.Text
            )
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        Button(
            onClick = {

                onSave(
                    type,
                    uiState.nickname
                )
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Save")
        }


        // -----------------------------------------------------
        // CANCEL / BACK
        // -----------------------------------------------------

        TextButton(
            onClick = onBack,

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Cancel")
        }
    }
}