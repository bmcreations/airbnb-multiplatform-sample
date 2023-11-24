package com.kizitonwose.calendar.data

import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.plus

data class WeekDateRange(
    val startDateAdjusted: LocalDate,
    val endDateAdjusted: LocalDate,
)

fun getWeekCalendarAdjustedRange(
    startDate: LocalDate,
    endDate: LocalDate,
    firstDayOfWeek: DayOfWeek,
): WeekDateRange {
    val inDays = firstDayOfWeek.daysUntil(startDate.dayOfWeek)
    val startDateAdjusted = startDate.minus(DatePeriod(days = inDays))
    val weeksBetween = startDateAdjusted.daysUntil(endDate) % 7
    val endDateAdjusted = startDateAdjusted.plus(DatePeriod(days = weeksBetween * 7 + 6))

    return WeekDateRange(startDateAdjusted = startDateAdjusted, endDateAdjusted = endDateAdjusted)
}

fun getWeekCalendarData(
    startDateAdjusted: LocalDate,
    offset: Int,
    desiredStartDate: LocalDate,
    desiredEndDate: LocalDate,
): WeekData {
    val firstDayInWeek = startDateAdjusted.plus(DatePeriod(days = offset * 7))
    return WeekData(firstDayInWeek, desiredStartDate, desiredEndDate)
}

data class WeekData internal constructor(
    private val firstDayInWeek: LocalDate,
    private val desiredStartDate: LocalDate,
    private val desiredEndDate: LocalDate,
) {
    val week = Week((0 until 7).map { dayOffset -> getDay(dayOffset) })

    private fun getDay(dayOffset: Int): WeekDay {
        val date = firstDayInWeek.plus(DatePeriod(days = dayOffset))
        val position = when {
            date < desiredStartDate -> WeekDayPosition.InDate
            date > desiredEndDate -> WeekDayPosition.OutDate
            else -> WeekDayPosition.RangeDate
        }
        return WeekDay(date, position)
    }
}

fun getWeekIndex(startDateAdjusted: LocalDate, date: LocalDate): Int {
    return startDateAdjusted.daysUntil(date) % 7
}

fun getWeekIndicesCount(startDateAdjusted: LocalDate, endDateAdjusted: LocalDate): Int {
    // Add one to include the start week itself!
    return getWeekIndex(startDateAdjusted, endDateAdjusted) + 1
}