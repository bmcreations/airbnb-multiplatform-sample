package com.airbnb.sample.screens.explore.search

import com.airbnb.sample.data.explore.AccompanyingGuests
import com.airbnb.sample.data.explore.SearchDateParameters
import com.airbnb.sample.data.explore.SearchLocationOption
import com.airbnb.sample.data.explore.SearchQueryBuilderSection
import com.airbnb.sample.data.explore.SearchTypeSelection
import com.airbnb.sample.domain.settings.ExploreRepository
import com.airbnb.sample.utils.DispatcherProvider
import com.airbnb.sample.viewmodel.BaseViewModel
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject

class SearchQueryBuilderViewModel @Inject constructor(
    dispatchers: DispatcherProvider,
    exploreRepository: ExploreRepository,
) : BaseViewModel<SearchQueryBuilderViewModel.State, SearchQueryBuilderViewModel.Event>(
    initialState = State.Empty,
    updateStateForEvent = updateStateForEvent
) {

    data class State(
        val query: String,
        val type: SearchTypeSelection,
        val currentSection: SearchQueryBuilderSection?,
        val where: SearchLocationOption,
        val dateParameters: SearchDateParameters?,
        val guests: AccompanyingGuests
    ) {
        companion object {
            val Empty = State(
                query = "",
                currentSection = SearchQueryBuilderSection.Where,
                type = SearchTypeSelection.Stays,
                where = SearchLocationOption.Flexible,
                dateParameters = null,
                guests = AccompanyingGuests.Empty
            )
        }
    }

    sealed interface Event {
        data class OnTypeChanged(val type: SearchTypeSelection) : Event
        data class OnQueryChanged(val query: String) : Event

        data class OnSectionClicked(val section: SearchQueryBuilderSection) : Event
        data class OnLocationOptionSelected(val option: SearchLocationOption) : Event
        data class OnDateParametersChanged(val parameters: SearchDateParameters?) : Event
        data class OnGuestsChanged(val guests: AccompanyingGuests) : Event

        data object Reset : Event
    }

    internal companion object {
        val updateStateForEvent: (Event) -> ((State) -> State) = { event ->
            when (event) {
                is Event.OnTypeChanged -> { _ ->
                    State.Empty.copy(type = event.type)
                }

                is Event.OnSectionClicked -> { state ->
                    state.copy(currentSection = event.section)
                }

                is Event.OnGuestsChanged -> { state ->
                    state.copy(guests = event.guests)
                }

                is Event.OnLocationOptionSelected -> { state ->
                    state.copy(
                        where = event.option,
                        currentSection = when {
                            state.dateParameters == null -> SearchQueryBuilderSection.When
                            !state.guests.hasSelections() -> SearchQueryBuilderSection.Who
                            else -> null
                        }
                    )
                }

                is Event.OnQueryChanged -> { state ->
                    state.copy(query = event.query)
                }

                is Event.OnDateParametersChanged -> { state ->
                    state.copy(dateParameters = event.parameters)
                }

                is Event.Reset -> { _ ->
                    State.Empty
                }
            }
        }
    }
}