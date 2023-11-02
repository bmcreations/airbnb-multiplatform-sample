package com.airbnb.sample.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.airbnb.sample.theme.dimens

object ToggleSwitchDefaults {
    private val UncheckedColor = Color(0x22222222)

    @Composable
    fun checkmarkIcon(modifier: Modifier = Modifier) {
        Icon(
            modifier = Modifier.padding(MaterialTheme.dimens.staticGrid.x1).then(modifier),
            imageVector = Icons.Rounded.Check,
            contentDescription = null
        )
    }

    @Composable
    fun uncheckedIcon(modifier: Modifier = Modifier) {
        Icon(
            modifier = Modifier.padding(MaterialTheme.dimens.staticGrid.x1).then(modifier),
            imageVector = Icons.Rounded.Close,
            contentDescription = null
        )
    }

    @Composable
    fun colors(
        checkedTrackColor: Color = MaterialTheme.colorScheme.onBackground,
        checkedIconColor: Color = checkedTrackColor,
        uncheckedTrackColor: Color = UncheckedColor,
        uncheckedIconColor: Color = uncheckedTrackColor,
        checkedThumbColor: Color = MaterialTheme.colorScheme.background,
        uncheckedThumbColor: Color = checkedThumbColor,
    ) = SwitchDefaults.colors(
        checkedTrackColor = checkedTrackColor,
        uncheckedTrackColor = uncheckedTrackColor,
        checkedIconColor = checkedIconColor,
        uncheckedIconColor = uncheckedIconColor,
        checkedThumbColor = checkedThumbColor,
        uncheckedThumbColor = uncheckedThumbColor,
        checkedBorderColor = checkedTrackColor,
        uncheckedBorderColor = Color.Transparent,
    )
}

@Composable
fun ToggleSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thumbContent: @Composable () -> Unit = {
        val alpha by animateFloatAsState(if (checked) 1f else 0f)
        ToggleSwitchDefaults.checkmarkIcon(Modifier.alpha(alpha))
    },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SwitchColors = ToggleSwitchDefaults.colors(),
) {
    androidx.compose.material3.Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        thumbContent = thumbContent,
        colors = colors
    )
}