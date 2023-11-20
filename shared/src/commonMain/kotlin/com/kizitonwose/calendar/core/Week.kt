package com.kizitonwose.calendar.core

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Week(val days: List<WeekDay>)