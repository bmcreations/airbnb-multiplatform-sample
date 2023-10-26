package com.airbnb.sample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.airbnb.sample.navigation.AppNavigation
import com.airbnb.sample.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        setStatusBarColor(MaterialTheme.colorScheme.background, isSystemInDarkTheme())
        AppNavigation()
    }
}

@Composable
expect fun setStatusBarColor(color: Color, isDark: Boolean)