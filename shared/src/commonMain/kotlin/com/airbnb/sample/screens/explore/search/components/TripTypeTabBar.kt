package com.airbnb.sample.screens.explore.search.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.sample.data.explore.SearchTypeSelection
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.secondaryText

@Composable
internal fun TripTypeSelector(
    modifier: Modifier = Modifier,
    selectedType: SearchTypeSelection,
    onTypeChanged: (SearchTypeSelection) -> Unit,
) {
    val choices = remember {
        listOf(SearchTypeSelection.Stays, SearchTypeSelection.Experiences)
    }
    val selectedIndex by remember(selectedType) {
        derivedStateOf { choices.indexOf(selectedType) }
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        ScrollableTabRow(
            modifier = Modifier,
            selectedTabIndex = choices.indexOf(selectedType),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedIndex])
                        .width(50.dp),
                    height = MaterialTheme.dimens.thickBorder,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            divider = {},
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            tabs = {
                with(SearchTypeSelection.Stays) {
                    val isSelected = selectedType == this
                    SearchTypeTab(
                        type = this,
                        isSelected = isSelected,
                    ) {
                        onTypeChanged(this)
                    }
                }

                with(SearchTypeSelection.Experiences) {
                    val isSelected = selectedType == this
                    SearchTypeTab(
                        type = this,
                        isSelected = isSelected,
                    ) {
                        onTypeChanged(this)
                    }
                }
            }
        )
    }
}

@Composable
private fun SearchTypeTab(
    modifier: Modifier = Modifier,
    type: SearchTypeSelection,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.onBackground
        else MaterialTheme.colorScheme.secondaryText
    )
    Tab(
        modifier = modifier,
        selected = isSelected,
        text = {
            Text(
                text = type.name,
                style = MaterialTheme.typography.bodySmall,
                color = color,
            )
        },
        onClick = onClick
    )
}