package com.airbnb.sample.screens.explore.search.dates

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.airbnb.sample.data.explore.DateWindowOffset
import com.airbnb.sample.data.explore.SearchDateParameters
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.VerticalScrollingCalendar
import com.airbnb.sample.utils.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

@Composable
internal fun CalendarWithOffsetSelection(
    modifier: Modifier = Modifier,
    parameters: SearchDateParameters?,
    onParametersChanged: (SearchDateParameters) -> Unit,
) {
    var selectedDates by remember(parameters) {
        mutableStateOf(
            when (parameters) {
                is SearchDateParameters.Dates -> {
                    parameters.selectedDates.map { it.toLocalDate() }
                }

                else -> emptyList()
            }
        )
    }

    var windowOffset by remember(parameters) {
        mutableStateOf(
            when (parameters) {
                is SearchDateParameters.Dates -> parameters.offset
                else -> DateWindowOffset.None
            }
        )
    }

    LaunchedEffect(selectedDates, windowOffset) {
        val dates =
            selectedDates.map { it.atStartOfDayIn(TimeZone.currentSystemDefault()) }

        val params = parameters as? SearchDateParameters.Dates
        if (dates != params?.selectedDates || windowOffset != params.offset) {
            onParametersChanged(
                SearchDateParameters.Dates(
                    selectedDates = dates,
                    offset = windowOffset
                )
            )
        }
    }

    Column(modifier = modifier) {
        VerticalScrollingCalendar(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.inset),
            startDate = Clock.System.now(),
            monthsToShow = 100,
            selectedDates = selectedDates,
            showSelectedRanges = true,
            onDateSelected = { date ->
                if (selectedDates.contains(date)) {
                    // remove
                    selectedDates -= date
                } else {
                    when (selectedDates.count()) {
                        0 -> selectedDates += date
                        1 -> {
                            // only one selected, see if we can make a range
                            val selectedDate = selectedDates.first()
                            if (date.toEpochDays() < selectedDate.toEpochDays()) {
                                // prior to selection, reset with this
                                selectedDates = listOf(date)
                            } else {
                                selectedDates += date
                            }
                        }

                        2 -> {
                            selectedDates = listOf(date)
                        }
                    }
                }
            }
        )
        Divider(color = MaterialTheme.colorScheme.outline)
        SlidingWindowSelection(
            modifier = Modifier.fillMaxWidth(),
            selectedWindow = windowOffset
        ) {
            windowOffset = it
        }
    }
}

@Composable
private fun SlidingWindowSelection(
    modifier: Modifier = Modifier,
    selectedWindow: DateWindowOffset,
    onWindowSelected: (DateWindowOffset) -> Unit,
) {
    val offsets = listOf(
        DateWindowOffset.None,
        DateWindowOffset.Count(1),
        DateWindowOffset.Count(2),
        DateWindowOffset.Count(3),
        DateWindowOffset.Count(4),
        DateWindowOffset.Count(5)
    )

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.inset,
            vertical = MaterialTheme.dimens.staticGrid.x2
        ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(offsets) { offset ->
            val isSelected = offset == selectedWindow
            val borderThickness by animateDpAsState(
                if (isSelected) MaterialTheme.dimens.thickBorder
                else MaterialTheme.dimens.border
            )

            val borderColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.outline
            )
            Text(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onWindowSelected(offset) }
                    .border(
                        width = borderThickness,
                        color = borderColor,
                        shape = CircleShape
                    )
                    .padding(
                        horizontal = MaterialTheme.dimens.staticGrid.x4,
                        vertical = MaterialTheme.dimens.staticGrid.x2
                    ),
                text = offset.label,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}