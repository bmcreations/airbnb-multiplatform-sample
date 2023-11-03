package com.airbnb.sample.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.utils.ui.addIf

object SettingItemDefaults {
    @Composable
    fun icon(
        modifier: Modifier = Modifier,
        painter: Painter
    ) {
        Icon(modifier = modifier, painter = painter, contentDescription = null)
    }

    val ContentPadding: PaddingValues
        @Composable get() = PaddingValues(
            horizontal = MaterialTheme.dimens.inset,
            vertical = MaterialTheme.dimens.staticGrid.x3
        )

    val TitleTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.bodySmall

    val DescriptionTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W400)
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = SettingItemDefaults.ContentPadding,
    showDivider: Boolean = true,
    dividerColor: Color = MaterialTheme.colorScheme.outline,
    icon: Painter,
    title: String,
    titleTextStyle: TextStyle = SettingItemDefaults.TitleTextStyle,
    descriptionTextStyle: TextStyle = SettingItemDefaults.DescriptionTextStyle,
) {
    SettingItem(
        modifier = modifier,
        icon = { SettingItemDefaults.icon(painter = icon) },
        contentPadding = contentPadding,
        title = title,
        titleTextStyle = titleTextStyle,
        descriptionTextStyle = descriptionTextStyle,
        showDivider = showDivider,
        dividerColor = dividerColor,
        onClick = null
    )
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = SettingItemDefaults.ContentPadding,
    showDivider: Boolean = true,
    dividerColor: Color = MaterialTheme.colorScheme.outline,
    icon: Painter,
    title: String,
    titleTextStyle: TextStyle = SettingItemDefaults.TitleTextStyle,
    descriptionTextStyle: TextStyle = SettingItemDefaults.DescriptionTextStyle,
    onClick: (() -> Unit)? = null,
) {
    SettingItem(
        modifier = modifier,
        icon = { SettingItemDefaults.icon(painter = icon) },
        contentPadding = contentPadding,
        title = title,
        titleTextStyle = titleTextStyle,
        descriptionTextStyle = descriptionTextStyle,
        showDivider = showDivider,
        dividerColor = dividerColor,
        onClick = onClick
    )
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    contentPadding: PaddingValues = SettingItemDefaults.ContentPadding,
    showDivider: Boolean = true,
    dividerColor: Color = MaterialTheme.colorScheme.outline,
    title: String,
    titleTextStyle: TextStyle = SettingItemDefaults.TitleTextStyle,
    descriptionTextStyle: TextStyle = SettingItemDefaults.DescriptionTextStyle,
    onClick: (() -> Unit)? = null,
) {
    SettingItem(
        modifier = modifier.addIf(onClick != null) { Modifier.clickable { onClick?.invoke() } },
        icon = icon,
        contentPadding = contentPadding,
        title = title,
        titleTextStyle = titleTextStyle,
        descriptionTextStyle = descriptionTextStyle,
        description = null,
        showDivider = showDivider,
        dividerColor = dividerColor,
        endSlot = {
            if (onClick != null) {
                Icon(Icons.Rounded.KeyboardArrowRight, contentDescription = null)
            }
        }
    )
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = SettingItemDefaults.ContentPadding,
    icon: (@Composable () -> Unit)? = null,
    description: String? = null,
    showDivider: Boolean = true,
    dividerColor: Color = MaterialTheme.colorScheme.outline,
    title: String,
    titleTextStyle: TextStyle = SettingItemDefaults.TitleTextStyle,
    descriptionTextStyle: TextStyle = SettingItemDefaults.DescriptionTextStyle,
    endSlot: @Composable RowScope.() -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = contentPadding,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                icon.invoke()
                Spacer(Modifier.requiredWidth(MaterialTheme.dimens.staticGrid.x3))
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x1),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = title,
                        style = titleTextStyle
                    )
                    endSlot()
                }
                if (description != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        text = description,
                        style = descriptionTextStyle,
                    )
                }
            }
        }
        if (showDivider) {
            Divider(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.inset),
                color = dividerColor
            )
        }
    }
}