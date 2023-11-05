package com.airbnb.sample.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

expect fun Instant.format(format: String): String