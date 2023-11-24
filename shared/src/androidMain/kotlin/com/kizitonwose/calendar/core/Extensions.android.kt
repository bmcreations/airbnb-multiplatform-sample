package com.kizitonwose.calendar.core

import kotlinx.datetime.DayOfWeek
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

actual fun firstDayOfWeekFromLocale(): DayOfWeek {
    return DayOfWeek(WeekFields.of(Locale.getDefault()).firstDayOfWeek.value)
}