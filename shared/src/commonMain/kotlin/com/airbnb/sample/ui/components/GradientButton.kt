package com.airbnb.sample.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.primaryToPrimaryVariantGradient
import com.airbnb.sample.theme.primaryVariant
import com.airbnb.sample.utils.ui.addIf
import com.airbnb.sample.utils.ui.plus

@Composable
fun GradientButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    brush: Brush = MaterialTheme.colorScheme.primaryToPrimaryVariantGradient,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        shape = shape,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
    ) {
        Box(
            modifier = modifier
                .addIf(enabled) {
                    Modifier.background(
                        brush = brush,
                        shape = shape,
                    )
                }
                .addIf(!enabled) {
                    Modifier.background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                }
                .clip(shape)
                .padding(ButtonDefaults.ContentPadding.plus(PaddingValues(vertical = MaterialTheme.dimens.staticGrid.x1))),
            contentAlignment = Alignment.Center
        ) {
            val textStyle = LocalTextStyle.current
            CompositionLocalProvider(LocalTextStyle provides textStyle.merge(TextStyle(color = MaterialTheme.colorScheme.onPrimaryContainer))) {
                content(
                    this@Button
                )
            }
        }
    }
}