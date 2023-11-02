package com.airbnb.sample.domain.settings

import android.content.Context
import com.airbnb.sample.data.settings.Currency
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import me.tatarka.inject.annotations.Inject
import java.util.Locale


@Inject
class AndroidSettingsProvider(
    private val context: Context
): SettingsProvider {
    override fun provide(): ObservableSettings {
        return SharedPreferencesSettings(
            context.getSharedPreferences(
                "settings",
                Context.MODE_PRIVATE
            )
        )
    }

    override fun defaultCurrency(): Currency {
        val locale = Locale.getDefault()
        val currency = java.util.Currency.getInstance(locale)
        return Currency.findWithCode(currency.currencyCode)
            ?: Currency.findWithSymbol(currency.symbol)
            ?: Currency.United_States_dollar
    }
}