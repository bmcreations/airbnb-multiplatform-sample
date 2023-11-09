package com.airbnb.sample.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.airbnb.sample.data.houses.Stay
import com.airbnb.sample.data.location.LatLong
import com.airbnb.sample.data.maps.CameraPositionBounds
import com.airbnb.sample.services.location.LocationProvider

@Composable
expect fun MapView(
    modifier: Modifier,
    userLocation: LatLong?,
    resultLocations: List<Stay.Minimal>,
    useTotalPrice: Boolean,
    onMarkerSelectionChange: (String?) -> Unit,
    onMapMoved: () -> Unit,
)