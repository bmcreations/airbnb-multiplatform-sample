package com.airbnb.sample.inject

import android.content.Context
import com.airbnb.sample.domain.settings.AndroidSettingsProvider
import com.airbnb.sample.domain.settings.SettingsProvider
import com.airbnb.sample.services.location.AndroidLocationProvider
import com.airbnb.sample.services.location.LocationProvider
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@AppScope
@Component
actual abstract class ApplicationComponent(
    @get:Provides val context: Context
): PlatformComponent {
    @Provides fun AndroidSettingsProvider.bind(): SettingsProvider = this
    @Provides fun AndroidLocationProvider.bind(): LocationProvider = this
}