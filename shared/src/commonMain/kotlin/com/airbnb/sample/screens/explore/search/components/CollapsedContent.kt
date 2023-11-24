package com.airbnb.sample.screens.explore.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.airbnb.sample.theme.secondaryText

@Composable
internal fun CollapsedContent(
    modifier: Modifier = Modifier,
    title: String, value: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W500),
            color = MaterialTheme.colorScheme.secondaryText
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W500),
        )
    }
}