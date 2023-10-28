package com.airbnb.sample.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.secondaryText

@Composable
fun LargeHeader(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: (@Composable () -> Unit)? = null,
) {
    Column(modifier = modifier) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.titleMedium) {
            title()
        }
        if (description != null) {
            Spacer(modifier = Modifier.requiredHeight(MaterialTheme.dimens.grid.x2))
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.secondaryText,
                )
            ) {
                description()
            }
        }
    }
}