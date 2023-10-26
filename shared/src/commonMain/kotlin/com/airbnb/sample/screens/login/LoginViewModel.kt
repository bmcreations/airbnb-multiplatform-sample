package com.airbnb.sample.screens.login

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.airbnb.sample.data.login.SocialType
import com.airbnb.sample.viewmodel.BaseViewModel
import com.airbnb.sample.utils.DispatcherProvider
import com.airbnb.sample.utils.EmailValidation
import com.airbnb.sample.viewmodel.launch
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


class LoginViewModel @Inject constructor(
    dispatchers: DispatcherProvider,
) : BaseViewModel<LoginViewModel.State, LoginViewModel.Event>(
    initialState = State(
        "",
        "",
        null,
        true,
        SocialType.available(true),
    ),
    updateStateForEvent = updateStateForEvent
) {
    data class State(
        val region: String,
        val textValue: String,
        val textFieldErrorMessage: String?,
        val isPhoneSelected: Boolean,
        val socialTypes: List<SocialType>,
    ) {
        val hasMetRequirements: Boolean
            get() = region.isNotEmpty() &&
                    (if (!isPhoneSelected) {
                        // TODO: validate real phone numbers
                        textValue.isNotEmpty()
                    } else {
                        // phone is enabled by default w/ an error state
                        true
                    })
    }

    sealed interface Event {
        data class OnCountrySelected(val region: String) : Event
        data class OnTextValueUpdated(val value: String) : Event
        data object SwitchToEmail : Event
        data object SwitchToPhone : Event
        data object SubmitLogin: Event
        data class UpdateTextFieldErrorMessage(val error: String): Event
        data object ClearTextFieldError: Event
    }

    init {
        viewModelScope.launch {
            dispatchEvent(Event.OnCountrySelected("United States (+1)"))
        }

        eventFlow
            .filterIsInstance<Event.SubmitLogin>()
            .map { stateFlow.value.textValue }
            .onEach { enteredValue ->
                val isPhone = stateFlow.value.isPhoneSelected
                if (isPhone && enteredValue.isEmpty()) {
                    dispatchEvent(dispatchers.Main, Event.UpdateTextFieldErrorMessage("Please enter a valid phone number."))
                    return@onEach
                } else if (!isPhone) {
                    if (!EmailValidation.matches(enteredValue)) {
                        dispatchEvent(dispatchers.Main, Event.UpdateTextFieldErrorMessage("Please enter a valid email address."))
                        return@onEach
                    }
                }

                // TODO:
            }.launchIn(viewModelScope.coroutineScope)

        eventFlow
            .onEach {
                when (it) {
                    is Event.OnTextValueUpdated,
                    Event.SwitchToEmail,
                    Event.SwitchToPhone -> {
                        dispatchEvent(dispatchers.Main, Event.ClearTextFieldError)
                    }
                    else -> Unit
                }
            }.launchIn(viewModelScope.coroutineScope)
    }

    companion object {
        val updateStateForEvent: (Event) -> ((State) -> State) = { event ->
            when (event) {
                is Event.OnCountrySelected -> { state -> state.copy(region = event.region) }
                is Event.OnTextValueUpdated -> { state -> state.copy(textValue = event.value) }
                is Event.SwitchToPhone -> { state ->
                    state.copy(
                        isPhoneSelected = true,
                        socialTypes = SocialType.available(true)
                    )
                }

                is Event.SwitchToEmail -> { state ->
                    state.copy(
                        isPhoneSelected = false,
                        socialTypes = SocialType.available(false),
                    )
                }

                is Event.SubmitLogin -> { state ->
                    state
                }

                is Event.UpdateTextFieldErrorMessage -> { state ->
                    state.copy(textFieldErrorMessage = event.error)
                }
                is Event.ClearTextFieldError -> { state ->
                    state.copy(textFieldErrorMessage = null)
                }
            }
        }
    }
}