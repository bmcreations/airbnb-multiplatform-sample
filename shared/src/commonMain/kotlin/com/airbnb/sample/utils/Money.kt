package com.airbnb.sample.utils

// hacky but does the trick
fun String.formatAsMoney(): String {
    var index = 1
    return this
        .takeIf { it.length > 3 }
        ?.reversed()
        ?.map { if (index++ % 3 == 0) "$it," else it }
        ?.joinToString("")
        ?.reversed()
        ?.removePrefix(",")
        ?: this
}