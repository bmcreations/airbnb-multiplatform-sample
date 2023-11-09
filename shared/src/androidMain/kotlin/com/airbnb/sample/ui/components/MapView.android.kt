package com.airbnb.sample.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.sample.data.houses.Stay
import com.airbnb.sample.data.location.LatLong
import com.airbnb.sample.data.maps.CameraPositionBounds
import com.airbnb.sample.services.location.LocationProvider
import com.airbnb.sample.utils.formatAsMoney
import com.airbnb.sample.utils.ui.addIf
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
actual fun MapView(
    modifier: Modifier,
    userLocation: LatLong?,
    resultLocations: List<Stay.Minimal>,
    useTotalPrice: Boolean,
    onMarkerSelectionChange: (String?) -> Unit,
    onMapMoved: () -> Unit,
) {
    val currentLocation by remember(userLocation) {
        derivedStateOf {
            userLocation?.let {
                LatLng(it.latitude, it.longitude)
            }
        }
    }


    val cameraPositionState = rememberCameraPositionState()

    val markerLocations = remember(resultLocations) {
        resultLocations.map { LatLng(it.location.latitude, it.location.longitude) }
    }

    LaunchedEffect(resultLocations) {
        if (resultLocations.isEmpty()) return@LaunchedEffect
        val builder = LatLngBounds.Builder()
        markerLocations.forEach { location -> builder.include(location) }
        cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(builder.build(), 80))
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.move(CameraUpdateFactory.newLatLng(it))
        }
    }

    var currentSelectedMarkerId by remember { mutableStateOf<String?>(null) }
    var previousSelectedMarkers by remember {
        mutableStateOf(emptyList<String>())
    }

    LaunchedEffect(currentSelectedMarkerId) {
        val id = currentSelectedMarkerId
        if (id != null) {
            previousSelectedMarkers += id
        }
        onMarkerSelectionChange(id)
    }

    val properties = remember(userLocation) {
        MapProperties(
            isMyLocationEnabled = userLocation != null,
        )
    }

    val uiSettings = remember {
        MapUiSettings(
            myLocationButtonEnabled = false,
            zoomGesturesEnabled = true,
            zoomControlsEnabled = false,
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        onMapClick = { currentSelectedMarkerId = null }
    ) {
        resultLocations.forEach { listing ->
            val location = LatLng(listing.location.latitude, listing.location.longitude)
            val markerState = rememberMarkerState(position = location)
            MarkerComposable(
                keys = arrayOf(listing.id),
                state = markerState,
                tag = listing.id,
                onClick = {
                    currentSelectedMarkerId = it.tag as? String
                    true
                }
            ) {
                PricePill(
                    price = "${listing.totalPriceOfStay()}".formatAsMoney(),
                    isSelected = currentSelectedMarkerId == listing.id,
                    wasPreviouslySelected = previousSelectedMarkers.contains(listing.id)
                )
            }
        }
    }
}

@Composable
private fun PricePill(
    price: String,
    isSelected: Boolean,
    wasPreviouslySelected: Boolean,
) {
    val color by animateColorAsState(
        when {
            isSelected -> Color.Black
            wasPreviouslySelected -> Color(0xFFE6E6E6)
            else -> Color.White
        }, label = "background color"
    )

    val contentColor by animateColorAsState(
        when {
            isSelected -> Color.White
            wasPreviouslySelected -> Color.Black
            else -> Color.Black
        }, label = "content color"
    )
    Box(
        modifier = Modifier
            .background(shape = CircleShape, color = color)
            .addIf(!isSelected) {
                Modifier.border(1.dp, MaterialTheme.colorScheme.outline, shape = CircleShape)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "\$$price",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W700),
            color = contentColor
        )
    }
}