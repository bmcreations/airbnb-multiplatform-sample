package com.airbnb.sample.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.sample.data.houses.Stay
import com.airbnb.sample.data.location.LatLong
import com.airbnb.sample.data.maps.MapSettings
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.utils.formatAsMoney
import com.airbnb.sample.utils.ui.addIf
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import org.lighthousegames.logging.logging

@Composable
actual fun MapView(
    modifier: Modifier,
    contentPadding: PaddingValues,
    mapSettings: MapSettings,
    userLocation: LatLong?,
    resultLocations: List<Stay.Minimal>,
    useTotalPrice: Boolean,
    onMarkerSelectionChange: (String?) -> Unit,
    onUpdateUserLocation: () -> Unit,
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

    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.move(CameraUpdateFactory.newLatLng(it))
        }
    }

    var markersToDraw by remember(resultLocations) {
        mutableStateOf(emptyList<Stay.Minimal>())
    }

    BoxWithConstraints(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            contentPadding = contentPadding,
            contentDescription = "Map view for properties with applied filters and search parameters",
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            onMapClick = { currentSelectedMarkerId = null }
        ) {
            markersToDraw.forEach { listing ->
                val location = LatLng(listing.location.latitude, listing.location.longitude)
                val markerState = rememberMarkerState(
                    position = location
                )
                MarkerComposable(
                    keys = arrayOf(
                        listing.id,
                        location,
                        useTotalPrice,
                        currentSelectedMarkerId ?: "",
                        previousSelectedMarkers
                    ),
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

            LaunchedEffect(markersToDraw) {
                if (markersToDraw.isEmpty()) return@LaunchedEffect
                val bounds = LatLngBounds.builder()
                val coordinates = markersToDraw.map { LatLng(it.location.latitude, it.location.longitude) }
                coordinates.forEach { location ->
                    bounds.include(location)
                }
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds.build(),
                        80,
                    )
                )
            }

            LaunchedEffect(resultLocations) {
                markersToDraw = resultLocations
            }
        }

        val scope = rememberCoroutineScope()
        MapA11yControls(
            modifier = Modifier
                .padding(top = contentPadding.calculateTopPadding())
                .align(Alignment.TopEnd),
            mapSettings = mapSettings,
            canZoomOut = { cameraPositionState.position.zoom < properties.maxZoomPreference },
            canZoomIn = { cameraPositionState.position.zoom > properties.minZoomPreference },
            canMove = { direction -> true }
        ) {
            when (it) {
                MapControlEvent.Locate -> { onUpdateUserLocation() }
                is MapControlEvent.Pan -> {
                    val cameraUpdate = when (it.direction) {
                        MapControlEvent.MovementDirection.Left -> CameraUpdateFactory.scrollBy(-50f, 0f)
                        MapControlEvent.MovementDirection.Up -> CameraUpdateFactory.scrollBy(0f, -50f)
                        MapControlEvent.MovementDirection.Right -> CameraUpdateFactory.scrollBy(50f, 0f)
                        MapControlEvent.MovementDirection.Down -> CameraUpdateFactory.scrollBy(0f, 50f)
                    }

                    logging("maps").d { "moving via a11y control $cameraUpdate" }
                    cameraPositionState.move(cameraUpdate)
                }
                MapControlEvent.ZoomIn -> {
                    scope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                    }
                }
                MapControlEvent.ZoomOut -> {
                    scope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                    }
                }
            }
        }
    }
}

@GoogleMapComposable
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
            .shadow(elevation = 8.dp, shape = CircleShape)
            .addIf(!isSelected) {
                Modifier.border(1.dp, MaterialTheme.colorScheme.outline, shape = CircleShape)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.dimens.staticGrid.x3,
                vertical = MaterialTheme.dimens.staticGrid.x3
            ),
            text = "\$$price",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W700),
            color = contentColor
        )
    }
}