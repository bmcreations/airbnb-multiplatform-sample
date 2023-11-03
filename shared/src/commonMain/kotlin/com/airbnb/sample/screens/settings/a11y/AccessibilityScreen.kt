package com.airbnb.sample.screens.settings.a11y

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.secondaryText
import com.airbnb.sample.ui.components.Row
import com.airbnb.sample.ui.components.ToggleSwitch
import com.airbnb.sample.viewmodel.screenViewModel

@Composable
fun Screens.Settings.Accessibility.RenderAccessibility() {
    Content(modifier = Modifier.fillMaxSize())
}

@Composable
private fun Screens.Settings.Accessibility.Content(
    modifier: Modifier = Modifier,
    viewModel: AccessibilityViewModel = screenViewModel(),
) {
    val state by viewModel.stateFlow.collectAsState()

    AccessibilityScreen(modifier = modifier, state = state, dispatch = viewModel::dispatchEvent)
}

@Composable
private fun AccessibilityScreen(
    modifier: Modifier = Modifier,
    state: AccessibilityViewModel.State,
    dispatch: (AccessibilityViewModel.Event) -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = MaterialTheme.dimens.inset),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x7)
    ) {
        // TODO: see how to pull this into [SettingsItem]
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f)
                    .padding(end = MaterialTheme.dimens.inset),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x3)
            ) {
                Text(
                    text = "Map zoom controls",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Zoom in and out with dedicated buttons",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondaryText)
                )
            }
            ToggleSwitch(
                checked = state.mapZoomControlsEnabled,
                onCheckedChange = {
                    dispatch(AccessibilityViewModel.Event.OnZoomControlsChanged(it))
                }
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f)
                    .padding(end = MaterialTheme.dimens.inset),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x3)
            ) {
                Text(
                    text = "Map pan controls",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Pan around the map with directional buttons",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondaryText)
                )
            }
            ToggleSwitch(
                checked = state.mapPanControlsEnabled,
                onCheckedChange = {
                    dispatch(AccessibilityViewModel.Event.OnPanControlsChanged(it))
                }
            )
        }
    }
}