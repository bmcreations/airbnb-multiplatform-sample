package com.airbnb.sample.screens.settings

import com.airbnb.sample.data.settings.Currency
import com.airbnb.sample.domain.settings.SettingsRepository
import com.airbnb.sample.utils.DispatcherProvider
import com.airbnb.sample.viewmodel.BaseViewModel
import com.airbnb.sample.viewmodel.launch
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class SettingsViewModel @Inject constructor(
    dispatchers: DispatcherProvider,
    settings: SettingsRepository,
) : BaseViewModel<SettingsViewModel.State, SettingsViewModel.Event>(
    initialState = State.Empty,
    updateStateForEvent = updateStateForEvent
) {

    data class State(
        val currency: Currency?,
        val availableCurrencies: List<Currency>,
        val translateToEnglish: Boolean,
        val version: String,
    ) {
        companion object {
            val Empty = State(
                currency = null,
                availableCurrencies = emptyList(),
                translateToEnglish = false,
                version = airbnb.shared.BuildConfig.APP_VERSION
            )
        }
    }

    sealed interface Event {
        data class OnCurrencyUpdated(val currency: Currency): Event
        data class OnCurrenciesLoaded(val currencies: List<Currency>): Event
        data class OnTranslationEnabled(val enabled: Boolean): Event
    }

    init {

        viewModelScope.launch {
            dispatchEvent(Event.OnCurrenciesLoaded(Currency.entries.toList()))
        }

        settings.currencyIsoCode.observe()
            .flowOn(dispatchers.IO)
            .mapNotNull { Currency.findWithCode(it) }
            .onEach {
                dispatchEvent(dispatchers.Main, Event.OnCurrencyUpdated(it))
            }.launchIn(viewModelScope.coroutineScope)

        settings.translateToEnglish.observe()
            .flowOn(dispatchers.IO)
            .onEach {
                dispatchEvent(dispatchers.Main, Event.OnTranslationEnabled(it))
            }.launchIn(viewModelScope.coroutineScope)

        eventFlow
            .filterIsInstance<Event.OnCurrencyUpdated>()
            .map { it.currency.code }
            .distinctUntilChanged()
            .onEach { settings.currencyIsoCode.set(it) }
            .launchIn(viewModelScope.coroutineScope)

        eventFlow
            .filterIsInstance<Event.OnTranslationEnabled>()
            .map { it.enabled }
            .distinctUntilChanged()
            .onEach {
                settings.translateToEnglish.set(it)
            }.launchIn(viewModelScope.coroutineScope)
    }

    internal companion object {
        val updateStateForEvent: (Event) -> ((State) -> State) = { event ->
            when (event) {
                is Event.OnCurrencyUpdated -> { state ->
                    state.copy(currency = event.currency)
                }
                is Event.OnTranslationEnabled -> { state ->
                    state.copy(translateToEnglish = event.enabled)
                }

                is Event.OnCurrenciesLoaded -> { state ->
                    state.copy(availableCurrencies = event.currencies)
                }
            }
        }
    }
}