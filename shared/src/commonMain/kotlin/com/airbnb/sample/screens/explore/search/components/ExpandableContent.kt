package com.airbnb.sample.screens.explore.search.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExpandableContent(
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    onClick: (() -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    border: BorderStroke = BorderStroke(0.5.dp, Color(0xFFDCDCDC).copy(alpha = 0.72f)),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: CardElevation = CardDefaults.cardElevation(
        defaultElevation = if (isActive) 10.dp else 4.dp
    ),
    content: @Composable () -> Unit,
) {
    if (onClick != null) {
        Card(
            modifier = modifier,
            shape = shape,
            elevation = elevation,
            border = border,
            interactionSource = interactionSource,
            onClick = onClick,
        ) {
            content()
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            elevation = elevation,
            border = border,
        ) {
            content()
        }
    }
}