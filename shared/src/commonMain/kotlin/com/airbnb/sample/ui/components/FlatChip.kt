package com.airbnb.sample.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.airbnb.sample.theme.dimens

@Composable
fun FlatChip(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    label: String,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.onBackground
        else MaterialTheme.colorScheme.outline
    )

    val borderWidth by animateDpAsState(
        if (selected) MaterialTheme.dimens.thickBorder
        else MaterialTheme.dimens.border
    )

    val contentAlpha by animateFloatAsState(
        if (enabled) 1f else ContentAlpha.disabled
    )


    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background.copy(alpha = contentAlpha),
                shape = CircleShape
            )
            .border(
                width = borderWidth,
                color = borderColor.copy(alpha = contentAlpha),
                shape = CircleShape
            ).clip(CircleShape)
            .clickable { onClick() }
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.dimens.staticGrid.x4,
                vertical = MaterialTheme.dimens.staticGrid.x2,
            ),
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = contentAlpha)
        )
    }
}