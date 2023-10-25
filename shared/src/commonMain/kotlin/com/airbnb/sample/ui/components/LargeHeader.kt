package com.airbnb.sample.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.sample.theme.dimens

@Composable
fun LargeHeader(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: (@Composable () -> Unit)? = null,
) {
    Column(modifier = modifier) {
        title()
        if (description != null) {
            Spacer(modifier = Modifier.requiredHeight(MaterialTheme.dimens.grid.x1))
            description()
        }
    }
}