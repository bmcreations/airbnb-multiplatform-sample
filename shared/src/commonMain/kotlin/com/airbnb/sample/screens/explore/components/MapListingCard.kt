package com.airbnb.sample.screens.explore.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.airbnb.sample.data.houses.Stay
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.Row
import com.airbnb.sample.utils.formatAsMoney
import com.airbnb.sample.utils.round
import com.airbnb.sample.utils.ui.debugBounds
import com.seiko.imageloader.rememberImagePainter
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListingCard(
    modifier: Modifier = Modifier,
    listing: Stay.Minimal?,
    useTotalPrice: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.height(IntrinsicSize.Min),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        onClick = onClick
    ) {
        if (listing == null) return@Card
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            val painter = rememberImagePainter(
                url = listing.imageUrls.first(),
            )

            Image(
                modifier = Modifier
                    .weight(0.33f)
                    .aspectRatio(1.08f),
                contentScale = ContentScale.FillBounds,
                painter = painter,
                contentDescription = null
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
                            append("total")
                        } else {
                            append("night")
                        }
                    }
                }
            }

            ListingContent(
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(0.67f),
                location = listing.displayLocation("United States"),
                duration = listing.printedDuration(),
                price = { priceDisplay },
                rating = listing.rating.round(2).toString()
            )
        }
    }
}

@Composable
private fun ListingContent(
    modifier: Modifier = Modifier,
    location: String,
    duration: String,
    price: () -> AnnotatedString,
    rating: String,
) {
    Row(
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.67f)
                .padding(
                    start = MaterialTheme.dimens.staticGrid.x3,
                    top = MaterialTheme.dimens.staticGrid.x3,
                    bottom = MaterialTheme.dimens.staticGrid.x3,
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = location,
                style = MaterialTheme.typography.bodySmall
                    .copy(fontWeight = FontWeight.W600)
            )
            // TODO: feed this description in from [Stay.Minimal]
            // when not available left in composition to preserve space
            Text(
                modifier = Modifier.alpha(0f),
                text = "Beautiful beaches",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = duration,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = price(),
                style = MaterialTheme.typography.labelSmall,
                textDecoration = TextDecoration.Underline,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(MaterialTheme.dimens.staticGrid.x3)
                .weight(0.33f),
            horizontalAlignment = Alignment.End
        ) {
            Icon(
                modifier = Modifier
                    .clickable {

                    },
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.weight(1f))
            RatingWithStarLabel(rating = rating,)
        }
    }
}