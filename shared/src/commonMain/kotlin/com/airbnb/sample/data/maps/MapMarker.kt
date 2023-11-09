package com.airbnb.sample.data.maps

import com.airbnb.sample.data.location.LatLong

class MapMarker(
    val key: String = "",
    val position: LatLong = LatLong(0.0, 0.0),
    val title: String = "",
    val alpha: Float = 1.0f,
    val subtitle: String = ""
)