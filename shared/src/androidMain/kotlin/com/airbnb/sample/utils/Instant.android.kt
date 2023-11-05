package com.airbnb.sample.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

actual fun Instant.format(format: String): String {
    val formatter = DateTimeFormatter.ofPattern(format, Locale.getDefault())
    val ldt = toLocalDateTime(TimeZone.currentSystemDefault())
    val threeTenLdt = LocalDateTime.of(ldt.year, ldt.monthNumber, ldt.dayOfMonth, ldt.hour, ldt.minute)
    return formatter.format(threeTenLdt)
}