package dev.bmcreations.template.screens.home

import com.rickclephas.kmm.viewmodel.coroutineScope
import dev.bmcreations.template.getPlatformName
import dev.bmcreations.template.utils.BaseViewModel
import dev.bmcreations.template.utils.DispatcherProvider
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class HomeViewModel @Inject constructor(
    dispatchers: DispatcherProvider,
) : BaseViewModel<HomeViewModel.State, HomeViewModel.Event>(
    updateStateForEvent = updateStateForEvent,
    initialState = State(false, "World")
) {

    data class State(
        val showImage: Boolean,
        val greeting: String,
    )

    sealed interface Event {
        data class ShowImage(val show: Boolean): Event
        data class UpdateGreeting(val name: String): Event
    }

    init {
        eventFlow
            .filterIsInstance<Event.ShowImage>()
            .filter { it.show }
            .onEach {
                dispatchEvent(Event.UpdateGreeting(getPlatformName()))
            }
            .launchIn(viewModelScope.coroutineScope)
    }

    companion object {
        val updateStateForEvent: (Event) -> ((State) -> State) = { event ->
            when (event) {
                is Event.ShowImage -> { state -> state.copy(showImage = event.show) }
                is Event.UpdateGreeting -> { state -> state.copy(greeting = event.name) }
            }
        }
    }
}