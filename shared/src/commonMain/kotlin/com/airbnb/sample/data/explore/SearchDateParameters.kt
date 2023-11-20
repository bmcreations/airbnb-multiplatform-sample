package com.airbnb.sample.data.explore

import com.airbnb.sample.utils.printed
import com.kizitonwose.calendar.core.DayOfWeekProgression
import com.kizitonwose.calendar.core.YearMonth
import com.kizitonwose.calendar.core.nextMonth
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

enum class SearchDateParameterType {
    Dates, Months, Flexible
}
sealed interface SearchDateParameters {
    val type: SearchDateParameterType

    data class Dates(val selectedRange: ClosedRange<Instant>, val offset: DateWindowOffset): SearchDateParameters {
        override val type: SearchDateParameterType = SearchDateParameterType.Dates
    }
    data class Months(val duration: Int): SearchDateParameters {
        override val type: SearchDateParameterType = SearchDateParameterType.Months
    }
    data class Flexible(val criteria: FlexibleDateCriteria): SearchDateParameters {
        override val type: SearchDateParameterType = SearchDateParameterType.Flexible
    }

    fun printed(): String {
        return when (this) {
            is Dates -> {
               return  when (offset) {
                    is DateWindowOffset.Count -> {
                        val moveBy = offset.count
                        val adjustedStart = selectedRange.start.minus(moveBy.days)
                        val adjustEnd = selectedRange.endInclusive.plus(moveBy.days)
                        adjustedStart.rangeTo(adjustEnd).printed()
                    }
                    DateWindowOffset.None -> selectedRange.printed()
                }
            }
            is Flexible -> {
                val dates = criteria.months
                if (dates.count() == 12) {
                    // anytime
                    return "Any ${criteria.length::class.simpleName?.lowercase()}"
                }

                val months = dates.map { it.monthName() }

                when (criteria.length) {
                    FlexibleDateCriteria.StayLength.Month -> {
                        if (months.count() > 1) {
                            return "Month in ${months.joinToString(",") { it.take(3) }}"
                        }
                        "Month in ${months.first()}"
                    }
                    FlexibleDateCriteria.StayLength.Week -> {
                        if (months.count() > 1) {
                            return "Week in ${months.joinToString(",") { it.take(3) }}"
                        }
                        "Week in ${months.first()}"
                    }
                    FlexibleDateCriteria.StayLength.Weekend -> {
                        if (months.count() > 1) {
                            return "Weekend in ${months.joinToString(",") { it.take(3) }}"
                        }
                        "Weekend in ${months.first()}"
                    }
                }
            }
            is Months -> {
                val currentMonth = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    .let { YearMonth(month = it.monthNumber, year = it.year) }

                val nextMonth = currentMonth.nextMonth
                val endMonth = nextMonth.plusMonths(duration)

                return "${nextMonth.printed(withDay = 1)} - ${endMonth.printed(withDay = 1)}"
            }
        }
    }
}
