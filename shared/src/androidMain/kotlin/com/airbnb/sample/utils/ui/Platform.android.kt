package com.airbnb.sample.utils.ui

actual fun getPlatformName(): String = "Android"

actual val Platform.usesCloseAffordanceOnSheets: Boolean
    get() = true