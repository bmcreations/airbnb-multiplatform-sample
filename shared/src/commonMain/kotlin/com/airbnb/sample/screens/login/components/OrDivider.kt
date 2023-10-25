package com.airbnb.sample.screens.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.sample.theme.dimens

internal fun LazyListScope.loginSelectionDivider() {
    item {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.weight(1f)
                    .height(MaterialTheme.dimens.thickBorder)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
            Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.staticGrid.x2),
                text = "or", style = MaterialTheme.typography.labelSmall
            )
            Box(
                modifier = Modifier.weight(1f)
                    .height(MaterialTheme.dimens.thickBorder)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
        }
    }
}