package com.kizitonwose.calendar.data

import kotlinx.datetime.DayOfWeek


actual fun DayOfWeek.daysUntil(other: DayOfWeek): Int {
    val symbols = org.threeten.bp.DayOfWeek.entries.map { it.name.uppercase() }
    val start = symbols.indexOf(this.name)
    val end = symbols.indexOf(other.name)

    return if (end < start) {
        7 - start + end
    } else {
        end - start
    }
}