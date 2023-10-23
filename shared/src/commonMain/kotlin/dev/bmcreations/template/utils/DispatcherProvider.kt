package dev.bmcreations.template.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    val Default: CoroutineDispatcher
    val Main: CoroutineDispatcher
    val IO: CoroutineDispatcher
}

class DefaultDispatchers : DispatcherProvider {
    override val Default: CoroutineDispatcher = Dispatchers.Default
    override val Main: CoroutineDispatcher = Dispatchers.Main
    override val IO: CoroutineDispatcher = applicationIoDispatcher.dispatcher()
}


interface Dispatcher {
    fun dispatcher(): CoroutineDispatcher
}



internal expect val applicationIoDispatcher: Dispatcher

