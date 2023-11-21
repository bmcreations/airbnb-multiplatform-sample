package com.airbnb.sample.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.utils.ui.addIf

@Composable
fun ToggleCounter(
    modifier: Modifier = Modifier,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.staticGrid.x3,
            Alignment.End
        ),
    ) {
        val tint by animateColorAsState(if (count > 0) Color(0x8A373737) else Color(0x1A373737))
        IconButton(
            modifier = Modifier.border(1.dp, tint, shape = CircleShape),
            enabled = count > 0,
            onClick = onDecrement
        ) {
            Icon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = "Decrease",
                tint = tint,
            )
        }

        var minWidth by remember {
            mutableStateOf(0.dp)
        }
        val density = LocalDensity.current

        val text by remember(minWidth, count) {
            derivedStateOf {
                if (minWidth == 0.dp) "8"
                else count.toString()
            }
        }

        Box(
            modifier = Modifier
                .addIf(minWidth == 0.dp) {
                    Modifier.onPlaced {
                        minWidth = with(density) { it.size.width.toDp() }
                    }
                }.addIf(minWidth != 0.dp) {
                    Modifier.widthIn(min = minWidth)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(
            modifier = Modifier.border(1.dp, Color(0x8A373737), shape = CircleShape),
            onClick = onIncrement
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Increase", tint = Color(0x8A373737))
        }
    }
}