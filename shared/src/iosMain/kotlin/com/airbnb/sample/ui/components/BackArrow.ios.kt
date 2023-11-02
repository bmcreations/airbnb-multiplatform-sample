package com.airbnb.sample.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun BackArrow(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    contentDescription: String?,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Icon(Icons.Rounded.ArrowBackIos, contentDescription = contentDescription)
    }
}