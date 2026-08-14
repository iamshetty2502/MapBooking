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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.shetty.mapbooking.presentation.components.AppHeader
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

    val mapState =
        rememberCameraPositionState()


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
    // MOVE MAP TO CURRENT LOCATION
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
     * The marker stays fixed in the center.
     *
     * The map moves underneath the marker.
     *
     * When the user stops moving:
     *
     * map center
     *      ↓
     * selected location
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
    // SCREEN
    // =========================================================

    Box(
        modifier = Modifier.fillMaxSize()
    ) {


        // =====================================================
        // GOOGLE MAP
        // =====================================================

        GoogleMap(
            modifier = Modifier.fillMaxSize(),

            cameraPositionState =
                mapState,

            properties =
                MapProperties(
                    isMyLocationEnabled =
                        uiState.currentLocation != null
                ),

            uiSettings =
                MapUiSettings(

                    myLocationButtonEnabled =
                        false,

                    zoomControlsEnabled =
                        false,

                    compassEnabled =
                        false,

                    mapToolbarEnabled =
                        false
                )
        )


        // =====================================================
        // HEADER
        // =====================================================

        AppHeader(
            title = "Map Booking"
        )


        // =====================================================
        // AQI
        // =====================================================
        uiState.selectedLocationDetails?.let { location ->
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(
                        top = 8.dp,
                        end = 12.dp
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "aqi",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "${location.aqi}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }


        // =====================================================
        // FIXED CENTER MARKER
        // =====================================================

        Text(
            text = "📍",

            fontSize = 34.sp,

            modifier =
                Modifier.align(
                    Alignment.Center
                )
        )


        // =====================================================
        // BOTTOM CONTROLS
        // =====================================================

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 12.dp
                )
        ) {


            // =================================================
            // LOCATION A
            // =================================================

            LocationButton(
                text =
                    uiState.aLocation?.displayName
                        ?: "Source",

                enabled =
                    uiState.aLocation != null,

                onClick = {

                    viewModel.onLocationAClicked()
                }
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // =================================================
            // LOCATION B
            // =================================================

            LocationButton(
                text =
                    uiState.bLocation?.displayName
                        ?: "Destination",

                enabled =
                    uiState.bLocation != null,

                onClick = {

                    viewModel.onLocationBClicked()
                }
            )


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            // =================================================
            // MAIN ACTION BUTTON
            // =================================================

            Button(
                onClick = {

                    when {

                        // -------------------------------------
                        // STEP 1
                        // -------------------------------------

                        uiState.canSetA -> {

                            viewModel.onAction(
                                MapAction.SetA
                            )
                        }


                        // -------------------------------------
                        // STEP 2
                        // -------------------------------------

                        uiState.canSetB -> {

                            viewModel.onAction(
                                MapAction.SetB
                            )
                        }


                        // -------------------------------------
                        // STEP 3
                        // -------------------------------------

                        else -> {

                            viewModel.onAction(
                                MapAction.Book
                            )
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),

                enabled =
                    !uiState.isLoadingLocation
            ) {

                Text(
                    text = when {

                        uiState.canSetA ->
                            "Select Source"

                        uiState.canSetB ->
                            "Select Destination"

                        else ->
                            "Book Now"
                    },
                    fontSize = 16.sp,
                    fontWeight =
                        FontWeight.Bold
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
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(
                        bottom = 130.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .background(
                        color = Color.White,
                        shape =
                            RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            )
        }
    }
}


// =============================================================
// LOCATION BUTTON
// =============================================================

@Composable
private fun LocationButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color =
                    if (enabled) {
                        Color.White
                    } else {
                        Color(0xFFF1F3F4)
                    },

                shape =
                    RoundedCornerShape(6.dp)
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(
                horizontal = 12.dp
            ),

        contentAlignment =
            Alignment.CenterStart
    ) {

        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight =
                FontWeight.Bold,
            maxLines = 1
        )
    }
}