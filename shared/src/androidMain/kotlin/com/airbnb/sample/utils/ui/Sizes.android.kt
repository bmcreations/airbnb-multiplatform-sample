package com.airbnb.sample.utils.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.Modifier

actual fun Modifier.modalHeight(): Modifier = this.fillMaxHeight(0.90f)