package com.airbnb.sample.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.sample.navigation.LocalPlatformNavigator
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.SettingItem
import com.airbnb.sample.ui.components.ToggleSwitch
import com.airbnb.sample.ui.components.ToggleSwitchDefaults
import com.airbnb.sample.viewmodel.screenViewModel

@Composable
internal fun Screens.Settings.RenderSettings() {
    Content(modifier = Modifier.fillMaxSize())
}

@Composable
private fun Screens.Settings.Content(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = screenViewModel(),
) {
    val state by viewModel.stateFlow.collectAsState()

    SettingsScreen(modifier = modifier, state = state, dispatch = viewModel::dispatchEvent)
}

@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: SettingsViewModel.State,
    dispatch: (SettingsViewModel.Event) -> Unit
) {
    val navigator = LocalPlatformNavigator.current
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = MaterialTheme.dimens.staticGrid.x12)
    ) {
        item(key = state.currency) {
            SettingItem(
                modifier = Modifier.clickable {
                    navigator.show(Screens.Settings.CurrencySelection)
                },
                title = "Currency",
                dividerColor = MaterialTheme.colorScheme.outlineVariant,
                endSlot = {
                    if (state.currency != null) {
                        Text(
                            text = "${state.currency.code} (${state.currency.symbol})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            )
        }

        item(key = state.translateToEnglish) {
            SettingItem(
                title = "Translation",
                description = "Automatically translate descriptions and reviews to english",
                dividerColor = MaterialTheme.colorScheme.outlineVariant,
                endSlot = {
                    ToggleSwitch(
                        checked = state.translateToEnglish,
                        onCheckedChange = {
                            dispatch(SettingsViewModel.Event.OnTranslationEnabled(it))
                        },
                        colors = ToggleSwitchDefaults.colors(
                            checkedTrackColor = MaterialTheme.colorScheme.tertiary,
                            checkedIconColor = MaterialTheme.colorScheme.tertiary,
                        ),
                        thumbContent = {
                            if (state.translateToEnglish) {
                                ToggleSwitchDefaults.checkmarkIcon()
                            } else {
                                ToggleSwitchDefaults.uncheckedIcon()
                            }
                        }
                    )
                }
            )
        }

        item {
            SettingItem(
                title = "Terms of Service",
                dividerColor = MaterialTheme.colorScheme.outlineVariant,
                onClick = {

                }
            )
        }

        item {
            SettingItem(
                title = "Privacy Policy",
                dividerColor = MaterialTheme.colorScheme.outlineVariant,
                onClick = {

                }
            )
        }

        item {
            SettingItem(
                title = "Your Privacy Choices",
                dividerColor = MaterialTheme.colorScheme.outlineVariant,
                onClick = {

                }
            )
        }

        item {
            SettingItem(
                title = "Version ${state.version}",
                showDivider = false
            )
        }
    }
}