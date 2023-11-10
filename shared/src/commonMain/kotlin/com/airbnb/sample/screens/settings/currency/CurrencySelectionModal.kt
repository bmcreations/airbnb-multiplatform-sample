package com.airbnb.sample.screens.settings.currency

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.airbnb.sample.data.settings.Currency
import com.airbnb.sample.navigation.LocalPlatformNavigator
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.screens.settings.SettingsViewModel
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.Modal
import com.airbnb.sample.ui.components.SettingItem
import com.airbnb.sample.ui.components.SettingItemDefaults
import com.airbnb.sample.viewmodel.screenViewModel

@Composable
fun Screens.Settings.CurrencySelection.RenderCurrencySelection() {
    Modal(
        title = name,
    ) {
        Content(
            modifier = Modifier,
        )
    }
}

@Composable
fun Screens.Settings.CurrencySelection.Content(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = screenViewModel()
) {
    val navigator = LocalPlatformNavigator.current
    val state by viewModel.stateFlow.collectAsState()

    val sortedCurrencies = remember(state.availableCurrencies, state.currency) {
        val selectedCurrency = state.currency ?: return@remember state.availableCurrencies
        state.availableCurrencies.toMutableList().apply {
            remove(selectedCurrency)
            add(0, selectedCurrency)
        }.toList()
    }
    
    val selectAsCurrency = { currency: Currency ->
        viewModel.dispatchEvent(
            SettingsViewModel.Event.OnCurrencyUpdated(
                currency
            )
        )
        navigator.hide()
    }
    
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(sortedCurrencies) { index, currency ->
            SettingItem(
                modifier = Modifier.clickable { selectAsCurrency(currency) },
                title = "${currency.displayName()} - ${currency.symbol}",
                titleTextStyle = SettingItemDefaults.TitleTextStyle.copy(
                    fontWeight = if (currency == state.currency) FontWeight.W600 else FontWeight.W400
                ),
                showDivider = index != state.availableCurrencies.lastIndex,
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.dimens.inset,
                    // account for the interactive size of the radio button to derive padding
                    vertical = MaterialTheme.dimens.staticGrid.x1,
                ),
                endSlot = {
                    RadioButton(
                        selected = currency == state.currency,
                        onClick = { selectAsCurrency(currency) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.onBackground,
                        )
                    )
                }
            )
        }
    }
}