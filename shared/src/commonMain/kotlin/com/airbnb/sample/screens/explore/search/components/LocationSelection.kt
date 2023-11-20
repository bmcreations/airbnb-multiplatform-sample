package com.airbnb.sample.screens.explore.search.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.sample.data.explore.SearchLocationOption
import com.airbnb.sample.screens.explore.experience.components.SearchBar
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.secondaryText
import com.airbnb.sample.utils.printed

@Composable
internal fun LocationSelection(
    modifier: Modifier = Modifier,
    locationOption: SearchLocationOption,
    onLocationOptionSelected: (SearchLocationOption) -> Unit,
    isActive: Boolean,
    onSearchClicked: () -> Unit,
) {
    Crossfade(modifier = modifier, targetState = isActive) { active ->
        if (active) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimens.inset),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.inset)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.inset),
                    text = "Where to?",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W800)
                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.inset)
                        .clickable { onSearchClicked() }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(vertical = MaterialTheme.dimens.staticGrid.x2),
                    contentAlignment = Alignment.Center
                ) {
                    SearchBar(
                        title = {
                            Text(
                                text = locationOption.displayLabel(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondaryText
                            )
                        }
                    )
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.inset),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.inset),
                ) {
                    items(SearchLocationOption.choices) { choice ->
                        val borderWidth by animateDpAsState(
                            if (locationOption == choice) MaterialTheme.dimens.thickBorder
                            else MaterialTheme.dimens.border
                        )

                        val borderColor by animateColorAsState(
                            if (locationOption == choice) MaterialTheme.colorScheme.onBackground
                            else MaterialTheme.colorScheme.outlineVariant
                        )

                        val textColor by animateColorAsState(
                            if (locationOption == choice) MaterialTheme.colorScheme.onBackground
                            else MaterialTheme.colorScheme.secondaryText
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x3)) {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxWidth(0.3f)
                                    .aspectRatio(1f)
                                    .clickable { onLocationOptionSelected(choice) }
                                    .background(MaterialTheme.colorScheme.background)
                                    .border(borderWidth, borderColor, MaterialTheme.shapes.medium)
                            )
                            Text(
                                text = choice.displayLabel(),
                                style = MaterialTheme.typography.bodySmall
                                    .copy(fontWeight = FontWeight.W600),
                                color = textColor
                            )
                        }
                    }
                }
            }
        } else {
            CollapsedContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.dimens.inset,
                        vertical = MaterialTheme.dimens.staticGrid.x6,
                    ),
                title = "Where",
                value = locationOption.displayLabel(),
            )
        }
    }
}