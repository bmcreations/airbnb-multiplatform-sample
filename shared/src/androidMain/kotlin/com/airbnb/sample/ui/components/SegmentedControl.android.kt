package com.airbnb.sample.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.utils.ui.NoRippleInteractionSource


@Composable
actual fun <T> SegmentedControl(
    modifier: Modifier,
    options: List<T>,
    selected: T,
    onOptionClicked: (T) -> Unit,
    titleForItem: (T) -> String,
) {
    val contentMargin = MaterialTheme.dimens.staticGrid.x2
    val offset by animateFloatAsState(
        when (options.indexOf(selected)) {
            0 -> 0f
            1 -> 1f
            2 -> 2f
            else -> 0f
        }
    )
    androidx.compose.foundation.layout.Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(
                Color(0xFFEEEEEF),
                CircleShape,
            )
            .drawWithContent {
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(
                        contentMargin.value + (size.width / 3f * offset),
                        contentMargin.value
                    ),
                    size = Size(
                        this.size.width / 3f - contentMargin.value * 2,
                        this.size.height - contentMargin.value * 2
                    ),
                    cornerRadius = CornerRadius(this.size.height / 2)
                )
                drawContent()
            }
            .clip(CircleShape),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        options.onEachIndexed { index, segment ->
            Box(
                Modifier
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .clickable(
                        enabled = true,
                        onClickLabel = titleForItem(segment),
                        indication = null,
                        role = Role.Button,
                        interactionSource = NoRippleInteractionSource()
                    ) { onOptionClicked(segment) }
                    .padding(contentMargin)
                    // Divide space evenly between all segments.
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = titleForItem(segment),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W600)
                )
            }
        }
    }
}