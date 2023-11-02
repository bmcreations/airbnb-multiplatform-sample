package com.airbnb.sample.inject

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

expect abstract class ApplicationComponent : PlatformComponent

val LocalAppComponent: ProvidableCompositionLocal<ApplicationComponent> =
    staticCompositionLocalOf { error("ApplicationComponent not initialized") }
