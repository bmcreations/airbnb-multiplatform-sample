package com.airbnb.sample.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.utils.ui.Row

object SettingItemDefaults {
    @Composable
    fun icon(
        modifier: Modifier = Modifier,
        painter: Painter
    ) {
        Icon(modifier = modifier, painter = painter, contentDescription = null)
    }
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    showDivider: Boolean = true,
    onClick: () -> Unit,
) {
    SettingItem(
        modifier = modifier,
        icon = { SettingItemDefaults.icon(painter = icon) },
        title = title,
        showDivider = showDivider,
        onClick = onClick
    )
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: String,
    showDivider: Boolean = true,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.clickable { onClick() }.then(modifier)) {
        Row(
            modifier = Modifier.padding(vertical = MaterialTheme.dimens.grid.x3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon()
            Text(
                modifier = Modifier
                    .padding(start = MaterialTheme.dimens.staticGrid.x4)
                    .weight(1f),
                text = title,
                style = MaterialTheme.typography.bodySmall
            )
            Icon(Icons.Rounded.KeyboardArrowRight, contentDescription = null)
        }
        if (showDivider) {
            Divider(color = MaterialTheme.colorScheme.outline)
        }
    }
}