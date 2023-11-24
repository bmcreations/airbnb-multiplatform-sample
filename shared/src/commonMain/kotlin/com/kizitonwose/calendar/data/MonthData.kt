package com.kizitonwose.calendar.data

import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.YearMonth
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.minus
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.plus
import org.lighthousegames.logging.logging

data class MonthData internal constructor(
    private val month: YearMonth,
    private val inDays: Int,
    private val outDays: Int,
) {

    private val totalDays = inDays + month.lengthOfMonth() + outDays

    private val firstDay = month.atStartOfMonth().minus(DatePeriod(days = inDays))

    private val rows = (0 until totalDays).chunked(7)

    private val previousMonth = month.previousMonth

    private val nextMonth = month.nextMonth

    val calendarMonth =
        CalendarMonth(month, rows.map { week -> week.map { dayOffset -> getDay(dayOffset) } })

    private fun getDay(dayOffset: Int): CalendarDay {
        val date = firstDay.plus(DatePeriod(days = dayOffset))
        val position = when (val ym = date.yearMonth) {
            month -> DayPosition.MonthDate
            previousMonth -> DayPosition.InDate
            nextMonth -> DayPosition.OutDate
            else -> throw IllegalArgumentException("Invalid date: $date, ym=$ym in month: $month :: $firstDay, $previousMonth, $nextMonth")
        }
        return CalendarDay(date, position)
    }
}

fun getCalendarMonthData(
    startMonth: YearMonth,
    offset: Int,
    firstDayOfWeek: DayOfWeek,
    outDateStyle: OutDateStyle,
): MonthData {
    val month = startMonth.plusMonths(offset)
    val firstDay = month.atStartOfMonth()
    val inDays = firstDayOfWeek.daysUntil(firstDay.dayOfWeek)
    val outDays = (inDays + month.lengthOfMonth()).let { inAndMonthDays ->
        val endOfRowDays = if (inAndMonthDays % 7 != 0) 7 - (inAndMonthDays % 7) else 0
        val endOfGridDays = if (outDateStyle == OutDateStyle.EndOfRow) 0 else run {
            val weeksInMonth = (inAndMonthDays + endOfRowDays) / 7
            return@run (6 - weeksInMonth) * 7
        }
        return@let endOfRowDays + endOfGridDays
    }
    return MonthData(month, inDays, outDays)
}

fun getMonthIndex(startMonth: YearMonth, targetMonth: YearMonth): Int {
    val start = startMonth.atStartOfMonth()
    val target = targetMonth.atStartOfMonth()
    return start.monthsUntil(target)
}

fun getMonthIndicesCount(startMonth: YearMonth, endMonth: YearMonth): Int {
    // Add one to include the start month itself!
    return getMonthIndex(startMonth, endMonth) + 1
}