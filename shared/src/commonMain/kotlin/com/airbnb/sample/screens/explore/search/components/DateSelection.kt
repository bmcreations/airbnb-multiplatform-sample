package com.airbnb.sample.screens.explore.search.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.airbnb.sample.data.explore.DateWindowOffset
import com.airbnb.sample.data.explore.SearchDateParameterType
import com.airbnb.sample.data.explore.SearchDateParameters
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.SegmentedControl
import com.airbnb.sample.ui.components.VerticalScrollingCalendar
import com.airbnb.sample.utils.toLocalDate
import com.airbnb.sample.utils.ui.NoRippleInteractionSource
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayOfWeekProgression
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.lighthousegames.logging.logging

@Composable
internal fun DateSelection(
    modifier: Modifier = Modifier,
    parameters: SearchDateParameters?,
    onParametersChanged: (SearchDateParameters) -> Unit,
    isActive: Boolean,
    onExpand: () -> Unit,
    onSkip: () -> Unit,
    onNext: () -> Unit,
) {
    val interactionSource by remember(isActive) {
        derivedStateOf {
            if (isActive) NoRippleInteractionSource()
            else MutableInteractionSource()
        }
    }

    ExpandableContent(
        modifier = modifier,
        isActive = isActive,
        interactionSource = interactionSource,
        onClick = if (!isActive) { { onExpand() } } else null,
    ) {
        Crossfade(targetState = isActive) { active ->
            if (active) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(top = MaterialTheme.dimens.inset),
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.inset),
                        text = "When's your trip?",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W700)
                    )

                    var selectedDates by remember(parameters) {
                        mutableStateOf(when (parameters) {
                            is SearchDateParameters.Dates -> {
                                val start = parameters.selectedRange.start.toLocalDate()
                                val end = parameters.selectedRange.endInclusive.toLocalDate()
                                listOf(start, end)
                            }
                            else -> emptyList()
                        })
                    }

                    var windowOffset by remember(parameters) {
                        mutableStateOf(
                            when (parameters) {
                                is SearchDateParameters.Dates -> parameters.offset
                                else -> DateWindowOffset.None
                            }
                        )
                    }

                    val dateSelectionTweaks = remember { SearchDateParameterType.entries }
                    var selectedTweak by remember(parameters) {
                        mutableStateOf(parameters?.type ?: SearchDateParameterType.Dates)
                    }

                    LaunchedEffect(selectedTweak, selectedDates, windowOffset) {
                        logging("dates").d { "tweak=$selectedTweak, dates=$selectedDates, offset=$windowOffset" }
                        when (selectedTweak) {
                            SearchDateParameterType.Dates -> {
                                if (selectedDates.isNotEmpty()) {
                                    if (selectedDates.count() == 2) {
                                        val dates =
                                            selectedDates.map { it.atStartOfDayIn(TimeZone.currentSystemDefault()) }
                                        val range = dates.first().rangeTo(dates.last())
                                        val params = parameters as? SearchDateParameters.Dates
                                        if (range != params?.selectedRange || windowOffset != params.offset) {
                                            onParametersChanged(
                                                SearchDateParameters.Dates(
                                                    selectedRange = range,
                                                    offset = windowOffset
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            SearchDateParameterType.Months -> {
                                // TODO:
                            }
                            SearchDateParameterType.Flexible -> {
                                // TODO:
                            }
                        }
                    }

                    SegmentedControl(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = MaterialTheme.dimens.inset)
                            .padding(vertical = MaterialTheme.dimens.inset),
                        options = dateSelectionTweaks,
                        selected = selectedTweak,
                        onOptionClicked = { selectedTweak = it },
                        titleForItem = { it.name }
                    )

                    VerticalScrollingCalendar(
                        modifier = Modifier
                            .weight(1f),
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
                    Divider(color = MaterialTheme.colorScheme.outline)
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(
                                horizontal = MaterialTheme.dimens.inset,
                                vertical = MaterialTheme.dimens.staticGrid.x3,
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.clickable { onSkip() },
                            text = "Skip",
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W500)
                        )

                        Button(
                            onClick = { onNext() },
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                            ),
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    horizontal = MaterialTheme.dimens.staticGrid.x4,
                                    vertical = MaterialTheme.dimens.staticGrid.x2
                                ),
                                color = Color.White,
                                text = "Next"
                            )
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
                    title = "When",
                    value = parameters?.printed() ?: "Any week",
                )
            }
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