package com.airbnb.sample.domain.settings

import com.airbnb.sample.data.settings.BooleanSetting
import com.airbnb.sample.data.settings.Currency
import com.airbnb.sample.data.settings.StringSetting
import com.russhwolf.settings.ObservableSettings
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.encodeToString
import me.tatarka.inject.annotations.Inject

interface SettingsProvider {
    fun provide(): ObservableSettings
    fun defaultCurrency(): Currency
}

@Inject
class SettingsRepository(
    settingsProvider: SettingsProvider
) {

    private val settings = settingsProvider.provide()

    val currency = StringSetting(settings, "currency", settingsProvider.defaultCurrency().code)
    val translateToEnglish = BooleanSetting(settings, "translate_to_english", true)

    fun clear() = settings.clear()
}