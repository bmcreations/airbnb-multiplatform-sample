package com.airbnb.sample.utils.ui

import androidx.compose.material3.MaterialTheme

actual fun getPlatformName(): String = "iOS"


actual val Platform.usesCloseAffordanceOnSheets: Boolean
    get() = true