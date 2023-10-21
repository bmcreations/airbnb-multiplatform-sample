package dev.bmcreations.template

import androidx.compose.runtime.Composable
import dev.bmcreations.template.App

actual fun getPlatformName(): String = "Android"

@Composable
fun MainView() = App()