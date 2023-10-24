package com.airbnb.sample.screens.login

import com.airbnb.sample.utils.BaseViewModel
import com.airbnb.sample.utils.DispatcherProvider
import me.tatarka.inject.annotations.Inject

class LoginViewModel @Inject constructor(
    dispatchers: DispatcherProvider,
) : BaseViewModel<LoginViewModel.State, LoginViewModel.Event>(
    initialState = State("", ""),
    updateStateForEvent = updateStateForEvent
) {
    data class State(
        val email: String,
        val password: String,
    )

    sealed interface Event {
        data class OnEmailChanged(val email: String): Event
        data class OnPasswordChanged(val password: String): Event
    }

    companion object {
        val updateStateForEvent: (Event) -> ((State) -> State) = { event ->
            when (event) {
                is Event.OnEmailChanged -> { state -> state.copy(email = event.email) }
                is Event.OnPasswordChanged -> { state -> state.copy(password = event.password) }
            }
        }
    }
}