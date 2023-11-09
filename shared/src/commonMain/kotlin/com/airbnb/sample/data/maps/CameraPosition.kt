package com.airbnb.sample.data.maps

import com.airbnb.sample.data.location.LatLong

class CameraPositionBounds(
    val coordinates: List<LatLong> = listOf(),
    val padding: Int = 0
)

class CameraPosition(
    val target: LatLong = LatLong(0.0, 0.0),
    val zoom: Float = 0f
)