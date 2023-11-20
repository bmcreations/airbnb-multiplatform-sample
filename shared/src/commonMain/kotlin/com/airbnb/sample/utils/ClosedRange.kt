package com.airbnb.sample.utils

import kotlinx.datetime.Instant

fun ClosedRange<Instant>.printed() =  "${start.format("MMM dd")} - ${endInclusive.format("MMM dd")}"