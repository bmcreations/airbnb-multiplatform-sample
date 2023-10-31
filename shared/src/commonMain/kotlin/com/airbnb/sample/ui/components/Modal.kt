package com.airbnb.sample.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.airbnb.sample.navigation.LocalPlatformNavigator
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.utils.ui.Platform
import com.airbnb.sample.utils.ui.modalHeight
import com.airbnb.sample.utils.ui.usesCloseAffordanceOnSheets

@Composable
fun Modal(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.modalHeight().background(MaterialTheme.colorScheme.background)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.grid.x2)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(
                            start = MaterialTheme.dimens.staticGrid.x2,
                            top = MaterialTheme.dimens.staticGrid.x2
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val navigator = LocalPlatformNavigator.current
                    if (Platform.usesCloseAffordanceOnSheets) {
                        IconButton(onClick = { navigator.hide() }) {
                            Icon(Icons.Rounded.Close, contentDescription = "Close")
                        }
                    } else {
                        Spacer(Modifier.minimumInteractiveComponentSize())
                    }
                }
                Box(
                    modifier = Modifier.align(Alignment.Center)
                        .padding(vertical = MaterialTheme.dimens.staticGrid.x1),
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = title,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W700),
                    )
                }
            }
            Divider(color = MaterialTheme.colorScheme.outline)
        }
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}