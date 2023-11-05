package com.airbnb.sample.screens.explore.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.airbnb.sample.data.houses.Stay
import com.airbnb.sample.screens.main.LocalBottomPadding
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.Row
import com.airbnb.sample.ui.components.ToggleSwitch
import com.airbnb.sample.utils.formatAsMoney
import com.airbnb.sample.utils.round
import com.seiko.imageloader.rememberImagePainter
import kotlin.math.roundToInt

@Composable
internal fun ListContent(
    modifier: Modifier = Modifier,
    results: List<Stay.Minimal>,
    totalPriceSelected: Boolean,
    showTotalSelector: Boolean,
    totalToggle: @Composable () -> Unit,
    viewListing: (Stay.Minimal) -> Unit,
    onFavoriteClick: () -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(bottom = LocalBottomPadding.current),
        contentPadding = PaddingValues(MaterialTheme.dimens.inset),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.inset)
    ) {
        item {
            AnimatedContent(
                targetState = showTotalSelector,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) {
                if (it) {
                    totalToggle()
                }
            }
        }

        items(results) {
            // TODO: pull location to determine current country
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                result = it,
                useTotalPrice = totalPriceSelected,
                onFavoriteClick = onFavoriteClick,
                onClick = { viewListing(it) }
            )
        }
    }
}

@Composable
internal fun TotalToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .border(
                width = MaterialTheme.dimens.border,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            ).clip(MaterialTheme.shapes.medium)
            .clickable { onCheckedChange(!checked) },
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.staticGrid.x4,
            vertical = MaterialTheme.dimens.staticGrid.x1,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Display total before taxes",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W600)
        )
        ToggleSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
private fun ListItem(
    modifier: Modifier = Modifier,
    result: Stay.Minimal,
    useTotalPrice: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x4)
    ) {
        Box {
            Imagery(result.imageUrls)
            FavoriteHeart(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(MaterialTheme.dimens.staticGrid.x4)
                    .clickable { onFavoriteClick() },
                isFavorite = result.isFavorite
            )
        }
        Metadata(result, useTotalPrice)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Imagery(images: List<String>) {
    val pagerState = rememberPagerState { images.count() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.08f)
            .background(
                MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.medium
            ).clip(MaterialTheme.shapes.medium),
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            beyondBoundsPageCount = 4,
        ) { page ->
            val imageUri = images[page]
            val painter = rememberImagePainter(
                url = imageUri,
            )

            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                painter = painter,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun Metadata(listing: Stay.Minimal, useTotalPrice: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.alignByBaseline(),) {
            Text(
                text = listing.displayLocation("United States"),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W600)
            )
            Text(
                text = "${listing.distanceFromUser} miles away",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "${listing.stayLength()} nights • ${listing.printedDuration()}",
                style = MaterialTheme.typography.labelSmall
            )

            val priceDisplay by remember(
                listing.usdPricePoint,
                listing.nextAvailabilityDates,
                useTotalPrice
            ) {
                derivedStateOf {
                    buildAnnotatedString {
                        val highlightedAmount = if (useTotalPrice) {
                            "${listing.totalPriceOfStay()}"
                        } else {
                            "${listing.usdPricePoint.roundToInt()}"
                        }.formatAsMoney()

                        withStyle(SpanStyle(fontWeight = FontWeight.W600)) {
                            append("$${highlightedAmount}")
                        }

                        append(" ")
                        if (useTotalPrice) {
                            append("total before taxes")
                        } else {
                            append("night")
                        }
                    }
                }
            }
            Text(
                text = priceDisplay,
                style = MaterialTheme.typography.labelSmall,
                textDecoration = TextDecoration.Underline,
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .alignByBaseline(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x1, Alignment.End)
        ) {
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = Icons.Rounded.Star,
                contentDescription = null
            )
            Text(
                text = listing.rating.round(2).toString(),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun FavoriteHeart(
    modifier: Modifier = Modifier,
    isFavorite: Boolean
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (isFavorite) {
            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = null,
                tint = Color.White,
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.40f),
            )
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = Color.White,
            )
        }
    }
}
