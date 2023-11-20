package com.kizitonwose.calendar.core

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * Represents a day on the calendar.
 *
 * @param date the date for this day.
 * @param position the [DayPosition] for this day.
 */
@Immutable
@Serializable
data class CalendarDay(val date: LocalDate, val position: DayPosition = DayPosition.MonthDate)