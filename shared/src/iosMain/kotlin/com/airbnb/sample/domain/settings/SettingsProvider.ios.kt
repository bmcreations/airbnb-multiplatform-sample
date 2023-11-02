package com.airbnb.sample.domain.settings

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import me.tatarka.inject.annotations.Inject
import platform.Foundation.NSUserDefaults

@Inject
class IOSSettingsProvider: SettingsProvider {
    override fun provide(): ObservableSettings {
        return NSUserDefaultsSettings(
            NSUserDefaults.standardUserDefaults
        )
    }
}