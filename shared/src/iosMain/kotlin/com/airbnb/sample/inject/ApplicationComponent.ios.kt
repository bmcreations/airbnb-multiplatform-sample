package com.airbnb.sample.inject

import com.airbnb.sample.domain.settings.IOSSettingsProvider
import com.airbnb.sample.domain.settings.SettingsProvider
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides


@AppScope
@Component
actual abstract class ApplicationComponent: PlatformComponent {

    @Provides
    fun IOSSettingsProvider.bind(): SettingsProvider = this

    companion object
}