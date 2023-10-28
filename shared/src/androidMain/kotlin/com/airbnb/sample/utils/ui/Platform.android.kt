package com.airbnb.sample.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

actual fun getPlatformName(): String = "Android"

actual val Platform.usesCloseAffordanceOnSheets: Boolean
    get() = false

actual val Platform.fontScale: Float
    @Composable get() {
        return LocalDensity.current.fontScale
    }