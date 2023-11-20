package com.airbnb.sample.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.airbnb.sample.utils.ui.navigationBars

@Composable
fun BottomSafeArea(
    modifier: Modifier = Modifier,
    zIndex: Float = 999f,
    color: Color = Color.Transparent,
) {
    Spacer(
        modifier = Modifier
            .windowInsetsBottomHeight(WindowInsets.navigationBars)
            .zIndex(zIndex)
            .fillMaxWidth()
            .background(color)
            .then(modifier)
    )
}