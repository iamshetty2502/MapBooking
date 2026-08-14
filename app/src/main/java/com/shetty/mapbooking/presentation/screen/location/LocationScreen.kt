package com.shetty.mapbooking.presentation.screen.location

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shetty.mapbooking.data.model.LocationDetails
import com.shetty.mapbooking.presentation.components.AppHeader

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
    viewModel: LocationViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()


    // =========================================================
    // INITIALIZE LOCATION
    // =========================================================

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


    // =========================================================
    // SCREEN
    // =========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 12.dp
            )
    ) {

        AppHeader(
            title = "Location Details"
        )
        // =====================================================
        // LOCATION HEADER
        // =====================================================

        Text(
            text = type.uppercase(),

            fontSize = 16.sp,

            fontWeight = FontWeight.Bold
        )


        Spacer(
            modifier = Modifier.height(4.dp)
        )


        Text(
            text =
                uiState.location?.name
                    ?: name,

            fontSize = 16.sp,

            fontWeight = FontWeight.Bold
        )


        // =====================================================
        // AQI
        // =====================================================

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "aqi",

            fontSize = 12.sp
        )

        Text(
            text =
                "${uiState.location?.aqi ?: aqi}",

            fontSize = 14.sp,

            fontWeight = FontWeight.Bold
        )


        // =====================================================
        // PUSH NICKNAME + BUTTON TO BOTTOM
        // =====================================================

        Spacer(
            modifier = Modifier.weight(1f)
        )


        // =====================================================
        // NICKNAME
        // =====================================================

        OutlinedTextField(
            value = uiState.nickname,

            onValueChange = {
                viewModel.onNicknameChanged(it)
            },

            modifier = Modifier.fillMaxWidth(),

            placeholder = {
                Text(
                    text = "nickname"
                )
            },

            singleLine = true,

            supportingText = {
                Text(
                    text =
                        "${uiState.nickname.length}/20"
                )
            },

            keyboardOptions = KeyboardOptions(
                keyboardType =
                    KeyboardType.Text
            )
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // =====================================================
        // V / SAVE BUTTON
        // =====================================================

        Button(
            onClick = {

                onSave(
                    type,
                    uiState.nickname
                )
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {

            Text(
                text = "V",

                fontSize = 18.sp,

                fontWeight = FontWeight.Bold
            )
        }
    }
}