package com.kizitonwose.calendar.core

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class WeekDay(val date: LocalDate, val position: WeekDayPosition)