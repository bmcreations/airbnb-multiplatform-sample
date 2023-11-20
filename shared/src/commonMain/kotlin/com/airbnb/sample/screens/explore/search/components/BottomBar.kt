package com.airbnb.sample.screens.explore.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.primarySecondaryToPrimaryTertiaryGradient
import com.airbnb.sample.ui.components.BottomSafeArea
import com.airbnb.sample.ui.components.GradientButton

@Composable
internal fun BottomBar(
    modifier: Modifier = Modifier,
    onClear: () -> Unit,
    onSearch: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().then(modifier),
        shadowElevation = 8.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.dimens.inset,
                        vertical = MaterialTheme.dimens.staticGrid.x3,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.clickable { onClear() },
                    text = "Clear all",
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W500)
                )

                GradientButton(
                    onClick = { onSearch() },
                    shape = MaterialTheme.shapes.medium,
                    brush = MaterialTheme.colorScheme.primarySecondaryToPrimaryTertiaryGradient,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Search, contentDescription = null)
                        Spacer(Modifier.requiredWidth(MaterialTheme.dimens.staticGrid.x1))
                        Text(
                            modifier = Modifier.padding(vertical = MaterialTheme.dimens.staticGrid.x1),
                            text = "Search"
                        )
                    }
                }
            }
            BottomSafeArea()
        }
    }
}