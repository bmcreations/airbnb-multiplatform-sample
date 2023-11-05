package com.airbnb.sample.inject

import cafe.adriel.voyager.core.model.ScreenModel
import com.airbnb.sample.screens.explore.ExploreViewModel
import com.airbnb.sample.screens.login.LoginViewModel
import com.airbnb.sample.screens.settings.SettingsViewModel
import com.airbnb.sample.screens.profile.a11y.AccessibilityViewModel
import me.tatarka.inject.annotations.Component

@Component
abstract class ViewModelComponent(
    // provides SettingsProvider
    @Component val platformComponent: PlatformComponent
): DataComponent() {
    abstract val loginViewModel: LoginViewModel
    abstract val settingsViewModel: SettingsViewModel
    abstract val accessibilityViewModel: AccessibilityViewModel
    abstract val exploreViewModel: ExploreViewModel

    inline fun <reified T: ScreenModel> getViewModel(): ScreenModel {
        return when (val clazz = T::class) {
            LoginViewModel::class -> loginViewModel
            SettingsViewModel::class -> settingsViewModel
            AccessibilityViewModel::class -> accessibilityViewModel
            ExploreViewModel::class -> exploreViewModel
            else -> throw NoSuchElementException("No viewModel found for ${clazz.simpleName}")
        }
    }
}