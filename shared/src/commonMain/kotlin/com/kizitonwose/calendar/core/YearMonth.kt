package com.kizitonwose.calendar.core

import com.kizitonwose.calendar.data.isLeapYear
import com.kizitonwose.calendar.data.now
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class YearMonth(
    val month: Int,
    val year: Int
) : Comparator<YearMonth>, Comparable<YearMonth> {

    companion object {
        fun now(): YearMonth {
            val current = LocalDate.now()
            return YearMonth(month = current.monthNumber, year = current.year)
        }

        fun of(date: Instant): YearMonth {
            val localDate = date.toLocalDateTime(TimeZone.currentSystemDefault())
            return YearMonth(month = localDate.monthNumber, year = localDate.year)
        }

        fun of(date: LocalDate): YearMonth {
            return YearMonth(month = date.monthNumber, year = date.year)
        }

        fun from(pair: Pair<Int, Int>): YearMonth {
            return YearMonth(pair.first, pair.second)
        }
    }

    fun lengthOfMonth(leapYear: Boolean = isLeapYear(year)): Int {
        return when (Month(month)) {
            Month.FEBRUARY -> if (leapYear) 29 else 28
            Month.APRIL,
            Month.JUNE,
            Month.SEPTEMBER,
            Month.NOVEMBER -> 30
            else -> 31
        }
    }

    fun plusMonths(count: Int): YearMonth {
        val date = atStartOfMonth()
        val result = date.plus(DatePeriod(months = count))
        return YearMonth(month = result.monthNumber, year = result.year)
    }

    fun minusMonths(count: Int): YearMonth {
        val date = atStartOfMonth()
        val result = date.minus(DatePeriod(months = count))
        return YearMonth(month = result.monthNumber, year = result.year)
    }

    override fun compare(a: YearMonth, b: YearMonth): Int {
        return a.atStartOfMonth().compareTo(b.atStartOfMonth())
    }

    override operator fun compareTo(other: YearMonth): Int {
        return (year - other.year)
            .takeIf { it != 0 } ?: return month - other.month
    }

    fun printed(withDay: Int? = null): String {
        return "${Month(month).name} ${withDay ?: ""}, $year"
    }

    fun monthName(): String = Month(month).name.lowercase().capitalize(

    )
    fun abbreviatedMonth(): String {
        return Month(month).name.lowercase().take(3).capitalize()
    }

    fun asPair(): Pair<Int, Int> = Pair(month, year)

}