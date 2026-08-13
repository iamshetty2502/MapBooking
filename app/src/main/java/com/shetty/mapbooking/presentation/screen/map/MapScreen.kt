package com.shetty.mapbooking.presentation.screen.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.shetty.mapbooking.presentation.navigation.NavigationEvent

@Composable
fun MapScreen(
    onNavigate: (NavigationEvent) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {

    LaunchedEffect(viewModel) {

        viewModel.navigationEvent.collect { event ->
            onNavigate(event)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "MapBooking"
        )

        Button(
            onClick = {
                viewModel.onLocationAClicked()
            }
        ) {
            Text("Open A")
        }

        Button(
            onClick = {
                viewModel.onLocationBClicked()
            }
        ) {
            Text("Open B")
        }

        Button(
            onClick = {
                viewModel.onBookClicked()
            }
        ) {
            Text("Book")
        }

        Button(
            onClick = {
                viewModel.onHistoryClicked()
            }
        ) {
            Text("History")
        }
    }
}