package dev.bmcreations.template.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class AndroidDispatcher : Dispatcher {
    override fun dispatcher(): CoroutineDispatcher = Dispatchers.IO
}

actual val applicationIoDispatcher: Dispatcher = AndroidDispatcher()