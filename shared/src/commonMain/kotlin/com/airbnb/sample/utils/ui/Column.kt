package com.airbnb.sample.utils.ui

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable

@Composable
fun <T> itemsWithDividers(
    items: List<T>,
    divider: @Composable () -> Unit = { Divider() },
    block: @Composable (T) -> Unit,
) {
    items.forEachIndexed { index, item ->
        block(item)
        if (index in 0 until items.lastIndex) {
            divider()
        }
    }
}

@Composable
fun <T> itemsWithDividersIndexed(
    items: List<T>,
    divider: @Composable () -> Unit = { Divider() },
    block: @Composable (index: Int, item: T) -> Unit,
) {
    items.forEachIndexed { index, item ->
        block(index, item)
        if (index in 0 until items.lastIndex) {
            divider()
        }
    }
}

@Composable
fun itemsWithDividers(
    count: Int,
    divider: @Composable () -> Unit = { Divider() },
    block: @Composable (Int) -> Unit,
) {
    (0 until count).forEach { index ->
        block(index)
        if (index in 0 until count - 1) {
            divider()
        }
    }
}