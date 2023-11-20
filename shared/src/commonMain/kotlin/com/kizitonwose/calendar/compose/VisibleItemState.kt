package com.kizitonwose.calendar.compose

import kotlinx.serialization.Serializable

@Serializable
internal class VisibleItemState(
    val firstVisibleItemIndex: Int = 0,
    val firstVisibleItemScrollOffset: Int = 0,
)