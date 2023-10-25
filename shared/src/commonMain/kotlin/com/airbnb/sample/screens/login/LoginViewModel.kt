package com.airbnb.sample.screens.login

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.airbnb.sample.data.login.SocialType
import com.airbnb.sample.viewmodel.BaseViewModel
import com.airbnb.sample.utils.DispatcherProvider
import com.airbnb.sample.viewmodel.launch
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource



class LoginViewModel @Inject constructor(
    dispatchers: DispatcherProvider,
) : BaseViewModel<LoginViewModel.State, LoginViewModel.Event>(
    initialState = State(
        "",
        "",
        true,
        SocialType.available(true),
    ),
    updateStateForEvent = updateStateForEvent
) {
    data class State(
        val region: String,
        val textValue: String,
        val isPhoneSelected: Boolean,
        val socialTypes: List<SocialType>,
    ) {
        val hasMetRequirements: Boolean
            get() = region.isNotEmpty() &&
                    // TODO: validate real phone numbers
                    textValue.isNotEmpty()
    }

    sealed interface Event {
        data class OnCountrySelected(val region: String) : Event
        data class OnTextValueUpdated(val value: String) : Event
        data object SwitchToEmail : Event
        data object SwitchToPhone : Event
    }

    init {
        viewModelScope.launch {
            dispatchEvent(Event.OnCountrySelected("United States (+1)"))
        }
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
            }
        }
    }
}