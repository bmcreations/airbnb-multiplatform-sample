package com.airbnb.sample.domain.settings

import com.airbnb.sample.data.settings.Currency
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import me.tatarka.inject.annotations.Inject
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.currencyCode
import platform.Foundation.currencySymbol
import platform.Foundation.currentLocale

@Inject
class IOSSettingsProvider: SettingsProvider {
    override fun provide(): ObservableSettings {
        return NSUserDefaultsSettings(
            NSUserDefaults.standardUserDefaults
        )
    }

    override fun defaultCurrency(): Currency {
        val locale = NSLocale.currentLocale()
        return Currency.findWithCode(locale.currencyCode().orEmpty())
            ?: Currency.findWithSymbol(locale.currencySymbol())
            ?: Currency.United_States_dollar
    }
}