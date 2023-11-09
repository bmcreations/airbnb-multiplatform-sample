package com.airbnb.sample.screens.explore

import com.airbnb.sample.data.houses.HouseType
import com.airbnb.sample.data.houses.Stay
import com.airbnb.sample.data.settings.Currency
import com.airbnb.sample.domain.settings.ExploreRepository
import com.airbnb.sample.domain.settings.SettingsRepository
import com.airbnb.sample.utils.DispatcherProvider
import com.airbnb.sample.viewmodel.BaseViewModel
import com.airbnb.sample.viewmodel.launch
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class ExploreViewModel @Inject constructor(
    dispatchers: DispatcherProvider,
    settings: SettingsRepository,
    explorer: ExploreRepository,
): BaseViewModel<ExploreViewModel.State, ExploreViewModel.Event>(
   initialState = State.Empty,
    updateStateForEvent = updateStateForEvent
) {
    data class State(
        val currency: Currency,
        val houseTypes: List<HouseType>,
        val selectedHouseType: HouseType?,
        val useTotal: Boolean,
        val results: List<Stay.Minimal>
    ) {
        companion object {
            val Empty = State(
                currency = Currency.United_States_dollar,
                houseTypes = emptyList(),
                selectedHouseType = null,
                useTotal = true,
                results = emptyList()
            )
        }
    }

    sealed interface Event {
        data class OnCurrencySet(val currency: Currency): Event
        data class OnHouseTypesLoaded(val types: List<HouseType>): Event
        data class OnHouseTypeSelected(val type: HouseType): Event
        data class OnShowTotalChanged(val enabled: Boolean): Event
        data class OnResultsLoaded(val results: List<Stay.Minimal>): Event
    }

    init {
        settings.currencyIsoCode
            .observe()
            .flowOn(dispatchers.IO)
            .mapNotNull { Currency.findWithCode(it) }
            .onEach { dispatchEvent(dispatchers.Main, Event.OnCurrencySet(it)) }
            .launchIn(viewModelScope.coroutineScope)


        viewModelScope.launch {
            flowOf(explorer.getAvailableStays())
                .flowOn(dispatchers.IO)
                .map { it.getOrNull() }
                .filterNotNull()
                .filterIsInstance<List<Stay.Minimal>>()
                .onEach { results ->
                    val types = results.map { it.type }.distinct()
                    dispatchEvent(
                        dispatchers.Main,
                        Event.OnHouseTypesLoaded(types),
                      )

                    dispatchEvent(
                        dispatchers.Main,
                        Event.OnResultsLoaded(results)
                    )
                }.launchIn(this)
        }
    }

    internal companion object {
        val updateStateForEvent: (Event) -> ((State) -> State) = { event ->
            when (event) {
                is Event.OnCurrencySet -> { state ->
                    state.copy(currency = event.currency)
                }
                is Event.OnHouseTypesLoaded -> { state ->
                    state.copy(
                        houseTypes = event.types,
                        selectedHouseType = event.types.firstOrNull()
                    )
                }
                is Event.OnHouseTypeSelected -> { state ->
                    state.copy(selectedHouseType = event.type)
                }
                is Event.OnResultsLoaded -> { state ->
                    state.copy(results = event.results)
                }
                is Event.OnShowTotalChanged -> { state ->
                    state.copy(useTotal = event.enabled)
                }
            }
        }
    }
}