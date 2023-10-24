package com.airbnb.sample.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.airbnb.sample.inject.ViewModelComponent
import com.airbnb.sample.inject.create
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.ViewModelScope
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel<ViewState : Any, Event : Any>(
    initialState: ViewState,
    private val updateStateForEvent: (Event) -> (ViewState.() -> ViewState),
    private val defaultDispatcher: CoroutineContext = Dispatchers.Default,
) : KMMViewModel(), ScreenModel {

    private val _eventFlow: MutableSharedFlow<Event> = MutableSharedFlow()
    val eventFlow: SharedFlow<Event> = _eventFlow.asSharedFlow()

    private val _stateFlow: MutableStateFlow<ViewState> = MutableStateFlow(initialState)
    val stateFlow: StateFlow<ViewState> = _stateFlow.asStateFlow()

    fun dispatchEvent(event: Event) {
        // While it would be more elegant to have the stateFlow observe the eventFlow it seems like
        // less overhead to directly update state flow since we have everything we need.
        setState(updateStateForEvent(event))
        viewModelScope.launch(defaultDispatcher) {
            _eventFlow.emit(event)
        }
    }

    suspend fun dispatchEvent(context: CoroutineContext, event: Event) {
        withContext(context) {
            dispatchEvent(event)
        }
    }

    private fun setState(update: ViewState.() -> ViewState) {
        _stateFlow.value = _stateFlow.value.update()
    }
}

fun ViewModelScope.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return coroutineScope.launch(context, start, block)
}

@Composable
inline fun <reified T : ScreenModel> Screen.screenViewModel(
    crossinline factory: @DisallowComposableCalls () -> T = {
        with (ViewModelComponent::class.create()) {
            getViewModel<T>()
        } as T
    }
): T = this.rememberScreenModel(tag = T::class.qualifiedName) { factory() }