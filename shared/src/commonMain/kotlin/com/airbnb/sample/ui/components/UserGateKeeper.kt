package com.airbnb.sample.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.primarySecondaryToPrimaryTertiaryGradient

// TODO: Look into refactor into a LazyListScope receiver to support swapping b/w auth and anonymous
@Composable
fun UserGateKeeper(
    modifier: Modifier = Modifier,
    title: String,
    messageHeading: String,
    messageBody: String,
    onLogin: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.grid.x4)
    ) {
        item {
            Spacer(modifier = Modifier.padding(top = MaterialTheme.dimens.staticGrid.x14))
        }
        item {
            LargeHeader(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.inset),
                title = {
                    Text(text = title)
                }
            )
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.inset),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x1)) {
                Text(text = messageHeading, style = MaterialTheme.typography.bodyLarge)
                Text(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    text = messageBody,
                    style = MaterialTheme.typography.bodySmall,
                    minLines = 2,
                )
            }
        }
        item {
            GradientButton(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.inset)
                    .padding(top = MaterialTheme.dimens.grid.x5),
                onClick = onLogin,
                shape = MaterialTheme.shapes.medium,
                brush = MaterialTheme.colorScheme.primarySecondaryToPrimaryTertiaryGradient,
            ) {
                Text(
                    modifier = Modifier.padding(vertical = MaterialTheme.dimens.staticGrid.x1),
                    text = "Log in"
                )
            }
        }
    }
}
