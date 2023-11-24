package com.airbnb.sample.screens.explore.search.dates

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.airbnb.sample.data.explore.FlexibleDateCriteria
import com.airbnb.sample.data.explore.SearchDateParameters
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.outlineSecondaryVariant
import com.airbnb.sample.theme.secondaryText
import com.airbnb.sample.ui.components.FlatChip
import com.airbnb.sample.utils.ui.addIf
import com.kizitonwose.calendar.core.YearMonth

@Composable
internal fun FlexibleDateSelection(
    modifier: Modifier = Modifier,
    parameters: SearchDateParameters?,
    onParametersChanged: (SearchDateParameters) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.inset)
    ) {
        Divider(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.inset),
            color = MaterialTheme.colorScheme.outlineSecondaryVariant
        )

        val options = remember { FlexibleDateCriteria.StayLength.options() }
        var stayLength by remember(parameters) {
            mutableStateOf(
                when (parameters) {
                    is SearchDateParameters.Flexible -> parameters.criteria.length
                    else -> FlexibleDateCriteria.StayLength.Weekend
                }
            )
        }

        val monthsToDisplay by remember {
            derivedStateOf {
                val now = YearMonth.now()
                now.rangeTo(now.plusMonths(12)).toList()
            }
        }

        var selectedMonths by remember(parameters) {
            mutableStateOf(
                when (parameters) {
                    is SearchDateParameters.Flexible -> parameters.criteria.months.sorted()
                    else -> emptyList()
                }
            )
        }

        LaunchedEffect(stayLength, selectedMonths) {
            val criteria = (parameters as? SearchDateParameters.Flexible)?.criteria
            if (stayLength != criteria?.length || selectedMonths != criteria.months) {
                onParametersChanged(
                    SearchDateParameters.Flexible(
                        criteria = FlexibleDateCriteria(
                            length = stayLength,
                            months = selectedMonths
                        ),
                    )
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.inset),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x1)
        ) {
            Text(
                modifier = Modifier.alignByBaseline(),
                text = "Stay for a",
                style = MaterialTheme.typography.bodySmall
                    .copy(fontWeight = FontWeight.W600)
            )
            Crossfade(stayLength) {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = "${stayLength::class.simpleName}",
                    style = MaterialTheme.typography.bodySmall
                        .copy(fontWeight = FontWeight.W600)
                )
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.inset),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.fastForEach { length ->
                FlatChip(
                    selected = length == stayLength,
                    label = length::class.simpleName.orEmpty()
                ) {
                    stayLength = length
                }
            }
        }

        Divider(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.inset),
            color = MaterialTheme.colorScheme.outlineSecondaryVariant
        )

        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.inset),
            text = selectedMonths
                .takeIf { it.isNotEmpty() }
                ?.let {
                    if (it == monthsToDisplay) "Go anytime"
                    else "Go in ${it.joinToString(", ") { ym -> ym.monthName() }}"
                } ?: "Go anytime",
            style = MaterialTheme.typography.bodySmall
                .copy(fontWeight = FontWeight.W600),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
        ) {
            var itemWidth by remember { mutableStateOf(0.dp) }
            val density = LocalDensity.current

            val scrollState = rememberScrollState()
            com.airbnb.sample.ui.components.Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.inset),
            ) {
                monthsToDisplay.fastForEach { monthOfYear ->
                    val isSelected = selectedMonths.contains(monthOfYear)
                    val borderWidth by animateDpAsState(
                        if (isSelected) MaterialTheme.dimens.thickBorder
                        else MaterialTheme.dimens.border
                    )

                    val borderColor by animateColorAsState(
                        if (isSelected) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.outline
                    )

                    Box(
                        modifier = Modifier
                            .onPlaced {
                                val width = with(density) { it.size.width.toDp() }
                                if (width > itemWidth) {
                                    itemWidth = width
                                }
                            }
                            .addIf(itemWidth > 0.dp) {
                                Modifier.width(itemWidth)
                            }
                            .border(
                                width = borderWidth,
                                color = borderColor,
                                shape = MaterialTheme.shapes.large
                            ).clip(MaterialTheme.shapes.large)
                            .clickable {
                                if (selectedMonths.contains(monthOfYear)) {
                                    selectedMonths -= monthOfYear
                                } else {
                                    selectedMonths += monthOfYear
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.dimens.staticGrid.x4,
                                vertical = MaterialTheme.dimens.inset
                            ),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x3),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector =
                                Icons.Outlined.CalendarToday, contentDescription = null
                            )
                            Text(
                                text = monthOfYear.monthName(),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = monthOfYear.year.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondaryText
                            )
                        }
                    }
                }
            }
        }
    }
}

private operator fun ClosedRange<YearMonth>.iterator(): Iterator<YearMonth> {
    return object : Iterator<YearMonth> {
        private var current: YearMonth? = start

        override fun hasNext(): Boolean = current != null

        override fun next(): YearMonth {
            val next = current
            val incremented = current?.plusMonths(1) ?: return next!!
            current = if (contains(incremented)) incremented else null
            return next!!
        }

    }
}

private fun ClosedRange<YearMonth>.toList(): List<YearMonth> {
    return iterator().asSequence()
        .map { it }
        .toList()
}