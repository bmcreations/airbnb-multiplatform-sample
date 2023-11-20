package com.kizitonwose.calendar.core

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import org.lighthousegames.logging.logging


fun daysOfWeek(firstDayOfWeek: DayOfWeek = firstDayOfWeekFromLocale()): List<DayOfWeek> {
    val pivot = 7 - firstDayOfWeek.ordinal
    val daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at the start position.
    return (daysOfWeek.takeLast(pivot) + daysOfWeek.dropLast(pivot))
}

class DateIterator(private val daysInOrder: List<DayOfWeek>): Iterator<DayOfWeek> {
    private var currentDayOfWeek: DayOfWeek? = daysInOrder.first()

    override fun hasNext() = currentDayOfWeek != null

    override fun next(): DayOfWeek {
        val next = currentDayOfWeek
        val index = daysInOrder.indexOf(currentDayOfWeek)
        currentDayOfWeek = daysInOrder.getOrNull(index + 1)
        return next!!
    }

}


class DayOfWeekProgression(
    private val daysInOrder: List<DayOfWeek>,
) : Iterable<DayOfWeek>, ClosedRange<DayOfWeek> {

    override val endInclusive: DayOfWeek
        get() = daysInOrder.last()
    override val start: DayOfWeek
        get() = daysInOrder.first()

    override fun iterator(): Iterator<DayOfWeek> = DateIterator(daysInOrder)
}

/**
 * Returns the first day of the week from the default locale.
 */
expect fun firstDayOfWeekFromLocale(): DayOfWeek

/**
 * Returns the last day of the week from the default locale.
 */
fun lastDayOfWeekFromLocale(): DayOfWeek = dayOfWeekRangeFrom().endInclusive

fun dayOfWeekRangeFrom(start: DayOfWeek = firstDayOfWeekFromLocale()): DayOfWeekProgression {
    val dates = daysOfWeek(start)
    return DayOfWeekProgression(dates)
}

/**
 * Returns a [LocalDate] at the start of the month.
 *
 * Complements [YearMonth.atEndOfMonth].
 */
fun YearMonth.atStartOfMonth(): LocalDate {
    return LocalDate(year = year, monthNumber = month, dayOfMonth = 1)
}

fun YearMonth.atEndOfMonth(): LocalDate {
    return atStartOfMonth().plus(DatePeriod(months = 1))
        .minus(DatePeriod(days = 1))
}

val LocalDate.yearMonth: YearMonth
    get() = YearMonth(month = month.number, year = year)

val YearMonth.nextMonth: YearMonth
    get() = this.plusMonths(1)

val YearMonth.previousMonth: YearMonth
    get() = this.minusMonths(1)