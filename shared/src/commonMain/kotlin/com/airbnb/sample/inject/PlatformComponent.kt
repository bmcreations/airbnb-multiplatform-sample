package com.airbnb.sample.inject

import com.airbnb.sample.domain.settings.SettingsProvider
import com.airbnb.sample.utils.Version


interface PlatformComponent {
    val settingsProvider: SettingsProvider
}