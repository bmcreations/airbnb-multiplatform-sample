package com.airbnb.sample.screens.explore.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.Row

/**
 *This is a modified implementation of [ExtendedFloatingActionButton] that allows us to reorder the
 * placement of the text and icon elements.
 */
@Composable
fun MapFab(
    modifier: Modifier = Modifier,
    alphaProvider: () -> Float,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .semantics { role = Role.Button }
            .graphicsLayer {
                this.alpha = alphaProvider()
            },
        shape = CircleShape,
        color = Color.Black,
        contentColor = Color.White,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        interactionSource = interactionSource,
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            ProvideTextStyle(
                MaterialTheme.typography.labelSmall,
            ) {
                Box(
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = MaterialTheme.dimens.staticGrid.x14,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        modifier = Modifier
                            .sizeIn(minWidth = 80.dp, maxWidth = 80.dp)
                            .padding(
                                horizontal = 20.dp,
                                vertical = MaterialTheme.dimens.staticGrid.x3
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            Text(
                                "Map",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                            Spacer(Modifier.width(MaterialTheme.dimens.staticGrid.x1))
                            Icon(
                                modifier = Modifier.size(MaterialTheme.dimens.staticGrid.x5),
                                imageVector = Icons.Rounded.Map,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                    )
                }
            }
        }
    }
}