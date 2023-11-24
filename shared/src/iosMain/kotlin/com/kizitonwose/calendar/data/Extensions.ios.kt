package com.kizitonwose.calendar.data

import kotlinx.datetime.DayOfWeek
import platform.Foundation.NSCalendar

actual fun DayOfWeek.daysUntil(other: DayOfWeek): Int {
    val symbols = (NSCalendar.currentCalendar.weekdaySymbols as List<String>)
        .map { it.uppercase() }

    val start = symbols.indexOf(this.name)
    val end = symbols.indexOf(other.name)

    return if (end < start) {
        7 - start + end
    } else {
        end - start
    }
}