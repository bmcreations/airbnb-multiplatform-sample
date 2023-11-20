package com.airbnb.sample.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.airbnb.sample.theme.dimens

object TabScope {
    val SpacerSize: Dp
        @Composable get() = MaterialTheme.dimens.staticGrid.x2

    @Composable
    fun renderItem(
        modifier: Modifier = Modifier,
        isSelected: Boolean = false,
        onClick: () -> Unit = { },
        content: @Composable ColumnScope.() -> Unit,
    ) {
        ScrollingTabBarDefaults.Tab(
            modifier = modifier,
            isSelected = isSelected,
            onClick = onClick,
            content = content
        )
    }
}

object ScrollingTabBarDefaults {
    @Composable
    fun Tab(
        modifier: Modifier = Modifier,
        isSelected: Boolean,
        onClick: () -> Unit,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        with(TabScope) {
            Box(
                modifier = modifier.clickable { onClick() },
                contentAlignment = Alignment.BottomCenter
            ) {
                val alpha by animateFloatAsState(if (isSelected) 1f else 0f)
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.thickBorder)
                    .background(MaterialTheme.colorScheme.onBackground)
                    .graphicsLayer {
                        this.alpha = alpha
                    },
                )

                Column(
                    modifier = Modifier.padding(bottom = SpacerSize),
                    verticalArrangement = Arrangement.spacedBy(SpacerSize),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    content()
                }

            }
        }
    }
}

@Composable
fun <T> ScrollingTabBar(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = MaterialTheme.dimens.inset),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x9),
    items: List<T>,
    isSelected: (T) -> Boolean = { false },
    onClick: (T) -> Unit,
    content: @Composable TabScope.(item: T, isSelected: Boolean) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
    ) {
        items(items) {
            with(TabScope) {
                val selected = isSelected(it)
                renderItem(isSelected = selected, onClick = { onClick(it) }) {
                    content(it, selected)
                }
            }
        }
    }
}

