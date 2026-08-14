package com.shetty.mapbooking.presentation.screen.location

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .navigationBarsPadding()
            .imePadding()
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        AppHeader(
            title = "Location Details"
        )


        // =====================================================
        // CONTENT
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    start = 16.dp,
                    top = 20.dp,
                    end = 16.dp,
                    bottom = 12.dp
                )
        ) {

            // =================================================
            // LOCATION TYPE
            // =================================================

            Text(
                text = type.uppercase(),

                fontSize = 16.sp,

                fontWeight = FontWeight.Bold,

                color = Color.Black
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            // =================================================
            // LOCATION NAME
            // =================================================

            Text(
                text =
                    uiState.location?.name
                        ?: name,

                fontSize = 16.sp,

                fontWeight = FontWeight.Bold,

                color = Color.Black
            )


            // =================================================
            // AQI
            // =================================================

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "aqi",

                fontSize = 12.sp,

                color = Color.Gray
            )

            Text(
                text =
                    "${uiState.location?.aqi ?: aqi}",

                fontSize = 14.sp,

                fontWeight = FontWeight.Bold,

                color = Color.Black
            )


            // =================================================
            // PUSH NICKNAME + BUTTON TO BOTTOM
            // =================================================

            Spacer(
                modifier = Modifier.weight(1f)
            )


            // =================================================
            // NICKNAME
            // =================================================

            OutlinedTextField(
                value = uiState.nickname,

                onValueChange = {
                    viewModel.onNicknameChanged(it)
                },

                modifier = Modifier.fillMaxWidth(),

                placeholder = {
                    Text(
                        text = "nickname",

                        color = Color.Gray
                    )
                },

                textStyle =
                    LocalTextStyle.current.copy(
                        color = Color.Black
                    ),

                singleLine = true,

                supportingText = {
                    Text(
                        text =
                            "${uiState.nickname.length}/20",

                        color = Color.Gray
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


            // =================================================
            // UPDATE BUTTON
            // =================================================

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
                    text = "Update",
                    fontSize = 14.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}