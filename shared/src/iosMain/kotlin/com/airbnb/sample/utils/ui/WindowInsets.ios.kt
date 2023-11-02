package com.airbnb.sample.utils.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
actual val WindowInsets.Companion.statusBars: WindowInsets
    @Composable
    @NonRestartableComposable
    get() = UIApplication
        .sharedApplication
        .keyWindow?.safeAreaInsets()?.useContents {
            WindowInsets(
                left = 0.dp,
                top = top.dp,
                bottom = 0.dp,
                right = 0.dp,
            )
        } ?: WindowInsets(0, 0, 0, 0)

@OptIn(ExperimentalForeignApi::class)
actual val WindowInsets.Companion.navigationBars: WindowInsets
    @Composable
    @NonRestartableComposable
    get() = UIApplication
        .sharedApplication
        .keyWindow?.safeAreaInsets()?.useContents {
            WindowInsets(
                left = 0.dp,
                top = 0.dp,
                right = 0.dp,
                bottom = bottom.dp,
            )
        } ?: WindowInsets(0, 0, 0, 0)