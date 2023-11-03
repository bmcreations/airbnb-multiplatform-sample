package com.airbnb.sample.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun TopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    windowInsets: WindowInsets,
    scrollBehavior: TopAppBarScrollBehavior?,
) {
    androidx.compose.material3.TopAppBar(
        title = {
            CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyLarge) {
                title()
            }
        },
        modifier,
        navigationIcon,
        actions,
        windowInsets,
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}