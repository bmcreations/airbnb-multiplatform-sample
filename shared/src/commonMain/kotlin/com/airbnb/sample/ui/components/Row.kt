package com.airbnb.sample.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.sample.utils.ui.calculateEndPadding
import com.airbnb.sample.utils.ui.calculateStartPadding
import com.airbnb.sample.utils.ui.calculateVerticalPadding

@Composable
inline fun Row(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit,
) {
    Box(modifier = modifier) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = contentPadding.calculateVerticalPadding()),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
        ) {
            Spacer(modifier = Modifier.requiredWidth(contentPadding.calculateStartPadding()))
            content()
            Spacer(modifier = Modifier.requiredWidth(contentPadding.calculateEndPadding()))
        }
    }
}