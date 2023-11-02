package com.airbnb.sample.domain.settings

import android.content.Context
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import me.tatarka.inject.annotations.Inject


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
}