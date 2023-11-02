package com.airbnb.sample.domain.settings

import com.airbnb.sample.data.settings.BooleanSetting
import com.airbnb.sample.data.settings.StringSetting
import com.russhwolf.settings.ObservableSettings
import me.tatarka.inject.annotations.Inject

interface SettingsProvider {
    fun provide(): ObservableSettings
}

@Inject
class SettingsRepository(settingsProvider: SettingsProvider) {

    private val settings = settingsProvider.provide()!!

    val currency = StringSetting(settings, "currency", "USD ($)")
    val translateToEnglish = BooleanSetting(settings, "translate_to_english", true)

    fun clear() = settings.clear()
}