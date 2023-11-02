package com.airbnb.sample.utils

import kotlin.jvm.JvmInline

expect fun getPlatformName(): String

object Platform
expect val Platform.usesCloseAffordanceOnSheets: Boolean
expect val Platform.fontScale: Float
expect val Platform.shouldUseSwipeBack: Boolean // on iOS, this would be true and on Android/Desktop, false

@JvmInline value class Version(val value: String)