package com.airbnb.sample.utils.ui

expect fun getPlatformName(): String

object Platform
expect val Platform.usesCloseAffordanceOnSheets: Boolean
expect val Platform.fontScale: Float