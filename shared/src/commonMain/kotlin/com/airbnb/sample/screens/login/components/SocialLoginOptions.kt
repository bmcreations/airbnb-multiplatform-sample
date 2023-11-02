package com.airbnb.sample.screens.login.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.sample.data.login.SocialType
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.fullOutline
import com.airbnb.sample.ui.components.Row

internal fun LazyListScope.socialLoginOptions(
    socialOptions: List<SocialType>,
    onTypeClicked: (SocialType) -> Unit
) {
    items(socialOptions) { option ->
        Row(
            modifier = Modifier.border(
                width = MaterialTheme.dimens.border,
                color = MaterialTheme.colorScheme.fullOutline,
                shape = MaterialTheme.shapes.medium
            ).clickable { onTypeClicked(option) },
            contentPadding = PaddingValues(
                vertical = MaterialTheme.dimens.staticGrid.x2,
                horizontal = MaterialTheme.dimens.staticGrid.x5
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = option.icon,
                contentDescription = null,
                tint = option.tint,
            )
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = "Continue with ${option.name}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W600),
            )
        }
    }
}