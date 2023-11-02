package com.airbnb.sample.utils.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable

actual val WindowInsets.Companion.statusBars: WindowInsets
    @Composable get() = WindowInsets.statusBars

actual val WindowInsets.Companion.navigationBars: WindowInsets
    @Composable get() = WindowInsets.navigationBars