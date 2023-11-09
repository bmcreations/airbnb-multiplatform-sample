package com.airbnb.sample.inject

import com.airbnb.sample.domain.settings.SettingsProvider
import com.airbnb.sample.services.location.LocationProvider


interface PlatformComponent {
    val settingsProvider: SettingsProvider
    val locationProvider: LocationProvider
}