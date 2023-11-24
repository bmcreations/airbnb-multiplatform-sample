package com.kizitonwose.calendar.core

import kotlinx.datetime.DayOfWeek
import platform.Foundation.NSCalendar


actual fun firstDayOfWeekFromLocale(): DayOfWeek {
    val dayNumber = NSCalendar.currentCalendar.firstWeekday.toInt()
    val symbols = (NSCalendar.currentCalendar.weekdaySymbols as List<String>)
        .map { it.uppercase() }
    val symbol = symbols[dayNumber - 1]
    return DayOfWeek.values().firstOrNull { it.name == symbol } ?: DayOfWeek.SUNDAY
}