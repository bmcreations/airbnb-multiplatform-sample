package com.airbnb.sample.extensions

import platform.Foundation.NSAttributedString
import platform.Foundation.NSAttributedStringKey
import platform.Foundation.NSMutableAttributedString
import platform.Foundation.appendAttributedString
import platform.Foundation.create

@Suppress("UNCHECKED_CAST")
inline fun NSAttributedString.Companion.create(
    text: String,
    vararg attributes: Pair<NSAttributedStringKey, Any>
): NSAttributedString =
    NSAttributedString.create(
        text,
        attributes.toMap() as Map<Any?, *>
    )

inline fun NSAttributedString.Companion.of(
    vararg strings: NSAttributedString
): NSAttributedString =
    NSMutableAttributedString.create("").apply {
        strings.forEach(::appendAttributedString)
    }