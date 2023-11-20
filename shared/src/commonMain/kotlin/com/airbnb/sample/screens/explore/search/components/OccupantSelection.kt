package com.airbnb.sample.screens.explore.search.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.sample.data.explore.AccompanyingGuests
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.secondaryText
import com.airbnb.sample.utils.ui.NoRippleInteractionSource

@Composable
internal fun OccupantSelection(
    modifier: Modifier = Modifier,
    guests: AccompanyingGuests,
    onGuestsChanged: (AccompanyingGuests) -> Unit,
    isActive: Boolean,
    onExpand: () -> Unit,
) {
    ExpandableContent(
        modifier = modifier,
        isActive = isActive,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isActive) 8.dp else 4.dp,
        ),
        interactionSource = remember { NoRippleInteractionSource() },
        onClick = { onExpand() }) {
        Crossfade(targetState = isActive) { active ->
            if (active) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.dimens.inset,
                            vertical = MaterialTheme.dimens.inset),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.inset)
                ) {
                    Text(
                        text = "Who's coming?",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W800)
                    )
                }
            } else {
                CollapsedContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.dimens.inset,
                            vertical = MaterialTheme.dimens.staticGrid.x6,
                        ),
                    title = "Who",
                    value = guests.toString().takeIf { it.isNotEmpty() } ?: "Add guests",
                )
            }
        }
    }
}