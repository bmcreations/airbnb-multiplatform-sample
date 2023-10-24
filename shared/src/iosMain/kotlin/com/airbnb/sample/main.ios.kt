package com.airbnb.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreImage.CIColor
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.UIKit.statusBarManager

actual fun getPlatformName(): String = "iOS"

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun setStatusBarColor(color: Color, isDark: Boolean) {
    val statusBarColor = UIColor(
        CIColor(
            color.red.toDouble(),
            color.green.toDouble(),
            color.green.toDouble(),
            color.alpha.toDouble(),
        ),
    )

    UIApplication
        .sharedApplication
        .keyWindow
        ?.windowScene
        ?.statusBarManager
        ?.statusBarFrame()
        ?.let {
            UIView(it)
        }?.apply {
            setBackgroundColor(statusBarColor)
        }?.let {
            UIApplication.sharedApplication.keyWindow?.addSubview(it)
        }
}

fun MainViewController() = ComposeUIViewController { App() }

