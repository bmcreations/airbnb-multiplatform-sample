package com.airbnb.sample.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.secondaryText
import com.airbnb.sample.utils.ui.addIf
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.YearMonth
import com.kizitonwose.calendar.core.atEndOfMonth
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.dayOfWeekRangeFrom
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.lastDayOfWeekFromLocale
import com.kizitonwose.calendar.data.now
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@Composable
fun VerticalScrollingCalendar(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(MaterialTheme.dimens.inset),
    showHeader: Boolean = true,
    startDate: Instant,
    monthsToShow: Int = 12,
    selectedDates: List<LocalDate> = emptyList(),
    showSelectedRanges: Boolean = false,
    onDateSelected: (LocalDate) -> Unit = { },
) {
    val start = remember(startDate) { YearMonth.of(startDate) }
    val today = remember { LocalDate.now() }
    val calendarState = rememberCalendarState(
        startMonth = start,
        endMonth = start.plusMonths(monthsToShow),
    )

    Column(modifier = modifier, verticalArrangement = verticalArrangement) {
        if (showHeader) {
            DayOfWeekHeader(modifier = Modifier.fillMaxWidth())
        }
        VerticalCalendar(
            modifier = Modifier.padding(contentPadding).weight(1f),
            state = calendarState,
            monthHeader = { month ->
                val name = Month(month.yearMonth.month).name
                    .lowercase()
                    .capitalize()

                Text(
                    modifier = Modifier.padding(top = MaterialTheme.dimens.staticGrid.x3),
                    text = "$name ${month.yearMonth.year}",
                    style = MaterialTheme.typography.bodySmall
                        .copy(fontWeight = FontWeight.W600)
                )
            },
            dayContent = { day ->
                val startOfMonth = YearMonth.of(day.date)
                    .plusMonths(1)
                    .atStartOfMonth()
                val endOfMonth = YearMonth.of(day.date)
                    .minusMonths(1)
                    .atEndOfMonth()

                // only render days in the given month (no in or out dates)
                if (day.position == DayPosition.MonthDate) {
                    val isTodayOrFuture = day.date.toEpochDays() >= today.toEpochDays()
                    val isSelected = day.date in selectedDates
                    val textColor by animateColorAsState(
                        if (isSelected) Color.White
                        else MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                            .drawSelectionsAndRange(day, selectedDates, showSelectedRanges)
                            .clip(CircleShape)
                            .clickable(enabled = isTodayOrFuture) { onDateSelected(day.date) }
                            .padding(MaterialTheme.dimens.staticGrid.x3),
                        text = day.date.dayOfMonth.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                            .copy(fontWeight = if (isTodayOrFuture) FontWeight.W600 else FontWeight.W500),
                        color = if (isTodayOrFuture) textColor else MaterialTheme.colorScheme.secondaryText,
                        textDecoration = if (!isTodayOrFuture) TextDecoration.LineThrough else null
                    )
                } else {
                    val range = if (selectedDates.count() > 1) {
                        selectedDates.first().rangeTo(selectedDates.last())
                    } else {
                        null
                    }

                    if (range != null && range.contains(day.date)) {
                        val month = YearMonth.of(day.date)
                        if (day.date.dayOfMonth == 1 || day.date.dayOfMonth == month.lengthOfMonth()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center)
                                    .graphicsLayer {
                                        compositingStrategy = CompositingStrategy.Offscreen
                                    }
                                    .drawCrossMonthGradient(day)
                                    .padding(MaterialTheme.dimens.staticGrid.x3),
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun DayOfWeekHeader(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .padding(
                start = MaterialTheme.dimens.staticGrid.x8,
                end = MaterialTheme.dimens.staticGrid.x11,
            ).then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val days = dayOfWeekRangeFrom(firstDayOfWeekFromLocale())

        days.forEach { day ->
            Text(
                text = day.name.first().toString(),
                color = MaterialTheme.colorScheme.secondaryText,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

private fun Modifier.drawSelectionsAndRange(
    forDay: CalendarDay,
    selectedDates: List<LocalDate>,
    showSelectedRanges: Boolean
) = composed {
    val backgroundColor = Color(0xFFF1F1F1)
    val isSelected = forDay.date in selectedDates

    val shape = MaterialTheme.shapes.medium
    val density = LocalDensity.current

    this.drawBehind {
        val range = if (selectedDates.count() > 1) {
            selectedDates.first().rangeTo(selectedDates.last())
        } else {
            null
        }
        if (range != null && range.contains(forDay.date) && showSelectedRanges) {
            val radius = size.minDimension / 2f
            val radiusDiff = radius - (size.height / 2)
            val yMin = -radiusDiff
            val yMax = size.height + radiusDiff
            val xMax = size.width
            val curveSize = xMax / 2

            val cornerRadius = CornerRadius(
                x = shape.topEnd.toPx(size, density),
                y = shape.topEnd.toPx(size, density),
            )

            when {
                range.start == forDay.date -> {
                    // start of week, round start
                    val path = Path().apply {
                        addRoundRect(
                            RoundRect(
                                rect = Rect(
                                    offset = Offset(xMax - curveSize + 1, yMin),
                                    size = Size(curveSize, yMax + radiusDiff - 10f),
                                ),
                                topLeft = cornerRadius,
                                bottomLeft = cornerRadius
                            )
                        )
                    }

                    drawPath(path, backgroundColor)
                }

                range.endInclusive == forDay.date -> {
                    val path = Path().apply {
                        addRoundRect(
                            RoundRect(
                                rect = Rect(
                                    offset = Offset(0f, yMin),
                                    size = Size(curveSize, yMax + radiusDiff - 10f)
                                ),
                                topRight = cornerRadius,
                                bottomRight = cornerRadius
                            )
                        )
                    }

                    drawPath(path, backgroundColor)
                }

                else -> {
                    when (forDay.date.dayOfWeek) {
                        firstDayOfWeekFromLocale() -> {
                            // start of week, round start
                            val path = Path().apply {
                                addRoundRect(
                                    RoundRect(
                                        rect = Rect(
                                            offset = Offset(0f, 0f),
                                            size = Size(
                                                width = size.width,
                                                height = size.height - 10f
                                            ),
                                        ),
                                        topLeft = cornerRadius,
                                        bottomLeft = cornerRadius
                                    )
                                )
                            }

                            drawPath(path, backgroundColor)
                        }

                        lastDayOfWeekFromLocale() -> {
                            // end of week, round end
                            val path = Path().apply {
                                addRoundRect(
                                    RoundRect(
                                        rect = Rect(
                                            offset = Offset(0f, 0f),
                                            size = Size(
                                                width = size.width,
                                                height = size.height - 10f
                                            ),
                                        ),
                                        topRight = cornerRadius,
                                        bottomRight = cornerRadius
                                    )
                                )
                            }

                            drawPath(path, backgroundColor)
                        }

                        else -> {
                            drawRect(
                                color = backgroundColor,
                                size = Size(width = size.width, height = size.height - 10f),
                            )
                        }
                    }
                }
            }
        }

        if (isSelected) {
            drawCircle(Color.Black)
        }
    }
}

private fun Modifier.drawCrossMonthGradient(day: CalendarDay) = composed {
    this.drawWithContent {
        if (day.position == DayPosition.OutDate) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFF1F1F1),
                        Color.Transparent,
                    )
                ),
                size = Size(width = size.width, height = size.height - 10f),
            )
        } else if (day.position == DayPosition.InDate) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFFF1F1F1),
                    )
                ),
                size = Size(width = size.width, height = size.height - 10f),
            )
        }
    }
}