package com.shetty.mapbooking.presentation.screen.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.shetty.mapbooking.presentation.navigation.NavigationEvent
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun MapScreen(
    onNavigate: (NavigationEvent) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {

    // =========================================================
    // CONTEXT
    // =========================================================

    val context = LocalContext.current


    // =========================================================
    // UI STATE
    // =========================================================

    val uiState by viewModel.uiState.collectAsState()


    // =========================================================
    // MAP STATE
    // =========================================================

    val mapState = rememberCameraPositionState()


    // =========================================================
    // LOCATION PERMISSION
    // =========================================================

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val fineLocationGranted =
                permissions[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true

            val coarseLocationGranted =
                permissions[
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ] == true

            if (
                fineLocationGranted ||
                coarseLocationGranted
            ) {
                viewModel.loadCurrentLocation()
            }
        }


    // =========================================================
    // REQUEST LOCATION PERMISSION
    // =========================================================

    LaunchedEffect(Unit) {

        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (
            fineLocationGranted ||
            coarseLocationGranted
        ) {

            viewModel.loadCurrentLocation()

        } else {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    // =========================================================
    // MOVE MAP TO USER'S INITIAL LOCATION
    // =========================================================

    LaunchedEffect(uiState.currentLocation) {

        uiState.currentLocation?.let { location ->

            mapState.position =
                CameraPosition.fromLatLngZoom(
                    location,
                    15f
                )
        }
    }


    // =========================================================
    // SELECTED LOCATION TRACKING
    // =========================================================

    /*
     * The map itself does not move a marker.
     *
     * The marker remains fixed in the center while the map
     * moves underneath it.
     *
     * Once the user stops moving the map:
     *
     * map center
     *      ↓
     * selectedLocation
     *      ↓
     * AQI + reverse geocoding
     */

    LaunchedEffect(mapState) {

        snapshotFlow {
            mapState.isMoving
        }
            .distinctUntilChanged()
            .filter { isMoving ->
                !isMoving
            }
            .collect {

                val selectedLocation =
                    mapState.position.target

                viewModel.onSelectedLocationChanged(
                    latitude =
                        selectedLocation.latitude,
                    longitude =
                        selectedLocation.longitude
                )

                viewModel.loadSelectedLocationDetails()
            }
    }


    // =========================================================
    // NAVIGATION EVENTS
    // =========================================================

    LaunchedEffect(viewModel) {

        viewModel.navigationEvent.collect { event ->

            onNavigate(event)
        }
    }


    // =========================================================
    // UI
    // =========================================================

    Box(
        modifier = Modifier.fillMaxSize()
    ) {


        // =====================================================
        // GOOGLE MAP
        // =====================================================

        GoogleMap(
            modifier = Modifier.fillMaxSize(),

            cameraPositionState = mapState,

            properties = MapProperties(
                isMyLocationEnabled =
                    uiState.currentLocation != null
            ),

            uiSettings = MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = false,
                compassEnabled = true
            )
        )


        // =====================================================
        // AQI
        // =====================================================

        uiState.selectedLocationDetails?.let { location ->

            Text(
                text = "AQI: ${location.aqi}",

                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = 16.dp,
                        end = 16.dp
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
            )
        }


        // =====================================================
        // FIXED CENTER MARKER
        // =====================================================

        Text(
            text = "📍",

            modifier = Modifier
                .align(Alignment.Center)
        )


        // =====================================================
        // SELECTED LOCATION DEBUG INFO
        // =====================================================

        /*
         * Temporary debug information.
         *
         * Keep this while developing.
         * We can remove it during Figma UI implementation.
         */

        uiState.selectedLocation?.let { location ->

            Text(
                text = buildString {

                    append(
                        "Lat: %.6f".format(
                            location.latitude
                        )
                    )

                    append("\n")

                    append(
                        "Lng: %.6f".format(
                            location.longitude
                        )
                    )
                },

                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
            )
        }


        // =====================================================
        // BOTTOM PANEL
        // =====================================================

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),

            verticalArrangement =
                Arrangement.Bottom
        ) {


            // =================================================
            // A LABEL
            // =================================================

            uiState.aLocation?.let { location ->

                Text(
                    text = location.displayName,

                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            viewModel.onLocationAClicked()
                        }
                        .padding(16.dp)
                )

            } ?: run {

                Text(
                    text = "A Location",

                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                )
            }


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // =================================================
            // B LABEL
            // =================================================

            uiState.bLocation?.let { location ->

                Text(
                    text = location.displayName,

                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            viewModel.onLocationBClicked()
                        }
                        .padding(16.dp)
                )

            } ?: run {

                Text(
                    text = "B Location",

                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                )
            }


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            // =================================================
            // SET A / SET B / BOOK
            // =================================================

            Button(
                onClick = {

                    val action = when {

                        uiState.canSetA ->
                            MapAction.SetA

                        uiState.canSetB ->
                            MapAction.SetB

                        else ->
                            MapAction.Book
                    }

                    viewModel.onAction(action)
                },

                modifier = Modifier.fillMaxWidth(),

                enabled =
                    !uiState.isLoadingLocation
            ) {

                Text(
                    text = uiState.buttonText
                )
            }
        }


        // =====================================================
        // LOADING
        // =====================================================

        if (uiState.isLoadingLocation) {

            CircularProgressIndicator(
                modifier =
                    Modifier.align(
                        Alignment.Center
                    )
            )
        }


        // =====================================================
        // ERROR
        // =====================================================

        uiState.error?.let { error ->

            Text(
                text = error,

                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = 180.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            )
        }
    }
}