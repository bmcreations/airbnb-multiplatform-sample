package com.airbnb.sample.screens.profile.a11y

import com.airbnb.sample.domain.settings.SettingsRepository
import com.airbnb.sample.screens.settings.SettingsViewModel
import com.airbnb.sample.utils.DispatcherProvider
import com.airbnb.sample.viewmodel.BaseViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class AccessibilityViewModel @Inject constructor(
    dispatchers: DispatcherProvider,
    settings: SettingsRepository
) : BaseViewModel<AccessibilityViewModel.State, AccessibilityViewModel.Event>(
    initialState = State(false, false),
    updateStateForEvent = updateStateForEvent,
) {

    init {
        settings.mapZoomControls
            .observe()
            .flowOn(dispatchers.IO)
            .onEach {
                dispatchEvent(dispatchers.Main, Event.OnZoomControlsChanged(it))
            }.launchIn(viewModelScope.coroutineScope)

        settings.mapPanControls
            .observe()
            .flowOn(dispatchers.IO)
            .onEach {
                dispatchEvent(dispatchers.Main, Event.OnPanControlsChanged(it))
            }.launchIn(viewModelScope.coroutineScope)

        eventFlow
            .filterIsInstance<Event.OnZoomControlsChanged>()
            .map { it.enabled }
            .distinctUntilChanged()
            .onEach {
                settings.mapZoomControls.set(it)
            }.launchIn(viewModelScope.coroutineScope)

        eventFlow
            .filterIsInstance<Event.OnPanControlsChanged>()
            .map { it.enabled }
            .distinctUntilChanged()
            .onEach {
                settings.mapPanControls.set(it)
            }.launchIn(viewModelScope.coroutineScope)
    }

    data class State(
        val mapZoomControlsEnabled: Boolean,
        val mapPanControlsEnabled: Boolean,
    )

    sealed interface Event {
        data class OnZoomControlsChanged(val enabled: Boolean): Event
        data class OnPanControlsChanged(val enabled: Boolean): Event
    }

    internal companion object {
        val updateStateForEvent: (Event) -> ((State) -> State) = { event ->
            when (event) {
                is Event.OnPanControlsChanged -> { state ->
                    state.copy(mapPanControlsEnabled = event.enabled)
                }
                is Event.OnZoomControlsChanged ->  { state ->
                    state.copy(mapZoomControlsEnabled = event.enabled)
                }
            }
        }
    }
}