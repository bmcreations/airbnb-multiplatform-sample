package com.airbnb.sample.screens.explore.experience.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.Row

@Composable
internal fun RatingWithStarLabel(
    modifier: Modifier = Modifier,
    rating: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.staticGrid.x1,
            Alignment.End
        )
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Icons.Rounded.Star,
            contentDescription = null
        )
        Text(
            text = rating,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
        )
    }
}