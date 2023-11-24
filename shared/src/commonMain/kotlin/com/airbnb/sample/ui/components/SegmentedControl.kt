package com.airbnb.sample.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun <T> SegmentedControl(
    modifier: Modifier = Modifier,
    options: List<T>,
    selected: T,
    onOptionClicked: (T) -> Unit,
    titleForItem: (T) -> String
)