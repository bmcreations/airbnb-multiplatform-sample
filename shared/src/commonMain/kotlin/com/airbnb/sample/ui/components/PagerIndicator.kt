package com.airbnb.sample.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.airbnb.sample.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    indicatorCount: Int = 5,
    indicatorSize: Dp = MaterialTheme.dimens.staticGrid.x4,
    indicatorShape: Shape = CircleShape,
    space: Dp = MaterialTheme.dimens.staticGrid.x2,
    activeColor: Color = Color.White.copy(alpha = 0.8f),
) {

    val listState = rememberLazyListState()

    val totalWidth: Dp = indicatorSize * indicatorCount + space * (indicatorCount - 1)
    val widthInPx = LocalDensity.current.run { indicatorSize.toPx() }

    val currentItem by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }

    val itemCount = pagerState.pageCount

    LaunchedEffect(key1 = currentItem) {
        val viewportSize = listState.layoutInfo.viewportSize
        listState.animateScrollToItem(
            currentItem,
            (widthInPx / 2 - viewportSize.width / 2).toInt()
        )
    }

    LazyRow(
        modifier = modifier.width(totalWidth),
        state = listState,
        contentPadding = PaddingValues(vertical = space),
        horizontalArrangement = Arrangement.spacedBy(space),
        userScrollEnabled = false
    ) {
        indicatorItems(
            itemCount,
            currentItem,
            indicatorCount,
            indicatorShape,
            activeColor,
            indicatorSize,
        )
    }
}

private fun LazyListScope.indicatorItems(
    itemCount: Int,
    currentItem: Int,
    indicatorCount: Int,
    indicatorShape: Shape,
    activeColor: Color,
    indicatorSize: Dp,
) {
    items(itemCount) { index ->

        val isSelected = (index == currentItem)

        // Index of item in center when odd number of indicators are set
        // for 5 indicators this is 2nd indicator place
        val centerItemIndex = indicatorCount / 2

        val right1 =
            (currentItem < centerItemIndex &&
                    index >= indicatorCount - 1)

        val right2 =
            (currentItem >= centerItemIndex &&
                    index >= currentItem + centerItemIndex &&
                    index < itemCount - centerItemIndex + 1)
        val isRightEdgeItem = right1 || right2

        // Check if this item's distance to center item is smaller than half size of
        // the indicator count when current indicator at the center or
        // when we reach the end of list. End of the list only one item is on edge
        // with 10 items and 7 indicators
        // 7-3= 4th item can be the first valid left edge item and
        val isLeftEdgeItem =
            index <= currentItem - centerItemIndex &&
                    currentItem > centerItemIndex &&
                    index < itemCount - indicatorCount + 1

        val scale = if (isSelected) {
            1f
        } else if (isLeftEdgeItem || isRightEdgeItem) {
            .5f
        } else {
            .8f
        }

        val color by animateColorAsState(Color.White.copy(alpha = 0.8f * scale))
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clip(indicatorShape)
                .size(indicatorSize)
                .background(
                    color,
                    indicatorShape
                )
        )
    }
}