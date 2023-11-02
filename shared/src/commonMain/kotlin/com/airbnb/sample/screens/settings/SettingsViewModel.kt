package com.airbnb.sample.screens.settings

import com.airbnb.sample.domain.settings.SettingsRepository
import com.airbnb.sample.utils.DispatcherProvider
import com.airbnb.sample.viewmodel.BaseViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
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
        val currency: String,
        val translateToEnglish: Boolean,
        val version: String,
    ) {
        companion object {
            val Empty = State(
                currency = "",
                translateToEnglish = false,
                version = airbnb.shared.BuildConfig.APP_VERSION
            )
        }
    }

    sealed interface Event {
        data class OnCurrencyUpdated(val currency: String): Event
        data class OnTranslationEnabled(val enabled: Boolean): Event
    }

    init {
        settings.currency.observe()
            .flowOn(dispatchers.IO)
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
            .onEach {
                settings.currency.set(it.currency)
            }.launchIn(viewModelScope.coroutineScope)

        eventFlow
            .filterIsInstance<Event.OnTranslationEnabled>()
            .onEach {
                settings.translateToEnglish.set(it.enabled)
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
            }
        }
    }
}