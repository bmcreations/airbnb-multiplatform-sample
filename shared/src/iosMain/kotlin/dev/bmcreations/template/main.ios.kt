package dev.bmcreations.template

import androidx.compose.ui.window.ComposeUIViewController
import dev.bmcreations.template.App

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App() }

