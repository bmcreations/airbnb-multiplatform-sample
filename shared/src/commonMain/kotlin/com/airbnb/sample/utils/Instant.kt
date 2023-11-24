package com.airbnb.sample.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

expect fun Instant.format(format: String): String

fun Instant.toLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()) =
    toLocalDateTime(timeZone).date