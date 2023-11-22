package com.airbnb.sample.screens.explore.search

import com.airbnb.sample.data.explore.AccompanyingGuests
import com.airbnb.sample.data.explore.DateWindowOffset
import com.airbnb.sample.data.explore.FlexibleDateCriteria
import com.airbnb.sample.data.explore.SearchDateParameterType
import com.airbnb.sample.data.explore.SearchDateParameters
import com.airbnb.sample.data.explore.SearchLocationOption
import com.airbnb.sample.data.explore.SearchQueryBuilderSection
import com.airbnb.sample.data.explore.SearchTypeSelection
import com.airbnb.sample.domain.settings.ExploreRepository
import com.airbnb.sample.utils.DispatcherProvider
import com.airbnb.sample.viewmodel.BaseViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.days

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

        fun hasDateSelections() = when (val params = dateParameters) {
            null -> false
            is SearchDateParameters.Dates -> params.selectedDates.isNotEmpty() || params.offset != DateWindowOffset.None
            is SearchDateParameters.Flexible -> params.criteria.length != FlexibleDateCriteria.StayLength.Weekend || params.criteria.months.isNotEmpty()
            is SearchDateParameters.Months -> params.duration != 3
        }

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

        data class Reset(
            val location: Boolean = true,
            val date: Boolean = true,
            val occupants: Boolean = true,
        ) : Event
    }

    init {
        eventFlow
            .filterIsInstance<Event.Reset>()
            .onEach {
                if (it.location) {
                    dispatchEvent(Event.OnLocationOptionSelected(SearchLocationOption.Flexible))
                }

                if (it.date) {
                    dispatchEvent(Event.OnDateParametersChanged(null))
                }

                if (it.occupants) {
                    dispatchEvent(Event.OnGuestsChanged(AccompanyingGuests.Empty))
                }

                if (it.location && it.date && it.occupants) {
                    dispatchEvent(Event.OnSectionClicked(SearchQueryBuilderSection.Where))
                }
            }.launchIn(viewModelScope.coroutineScope)
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

                is Event.Reset -> { state ->
                    state
                }
            }
        }
    }
}