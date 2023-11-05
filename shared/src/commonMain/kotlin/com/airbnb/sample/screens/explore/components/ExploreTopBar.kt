package com.airbnb.sample.screens.explore.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.sample.screens.explore.ExploreViewModel
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.secondaryText
import com.airbnb.sample.ui.components.Row
import com.airbnb.sample.ui.components.ScrollingTabBar

@Composable
internal fun TopBar(
    state: ExploreViewModel.State,
    dispatch: (ExploreViewModel.Event) -> Unit,
    onSizeDetermined: (Dp) -> Unit,
) {
    val density = LocalDensity.current
    Surface(
        modifier = Modifier.fillMaxWidth()
            .onPlaced { with(density) { onSizeDetermined(it.size.height.toDp()) } },
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 1.dp,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x5)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.inset),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x3),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FloatingSearchBar(
                    modifier = Modifier.weight(1f).padding(top = 0.5.dp)
                ) { }
                FilterButton { }
            }
            ScrollingTabBar(
                modifier = Modifier.fillMaxWidth(),
                items = state.houseTypes,
                isSelected = { state.selectedHouseType == it },
                onClick = {
                    dispatch(ExploreViewModel.Event.OnHouseTypeSelected(it))
                }
            ) { type, selected ->
                val baseColor = MaterialTheme.colorScheme.onBackground
                val alpha by animateFloatAsState(if (selected) 1f else 0.54f)

                Image(
                    modifier = Modifier.size(25.dp),
                    painter = type.icon,
                    contentDescription = type.label,
                    colorFilter = ColorFilter.tint(baseColor.copy(alpha))
                )
                Text(
                    text = type.label,
                    style = MaterialTheme.typography.labelSmall
                        .copy(
                            color = baseColor.copy(alpha),
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FloatingSearchBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        border = BorderStroke(0.5.dp, Color(0xFFDCDCDC).copy(alpha = 0.72f)),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.dimens.staticGrid.x4,
                    vertical = MaterialTheme.dimens.staticGrid.x2
                ).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x2),
        ) {
            Icon(Icons.Rounded.Search, contentDescription = null)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x1)
            ) {
                Text(
                    text = "Where to?",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    "Anywhere • Any week • Add guests",
                    style = MaterialTheme.typography.labelSmall
                        .copy(
                            color = MaterialTheme.colorScheme.secondaryText,
                            fontWeight = FontWeight.W400,
                            fontSize = 10.sp
                        )
                )
            }
        }
    }
}

@Composable
private fun FilterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .border(
                width = MaterialTheme.dimens.border,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.outline
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.padding(MaterialTheme.dimens.staticGrid.x2),
            imageVector = Icons.Rounded.Tune,
            contentDescription = "Filters"
        )
    }
}