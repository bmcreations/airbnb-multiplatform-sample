package dev.bmcreations.template.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


class IOSDispatcher : Dispatcher {
    override fun dispatcher(): CoroutineDispatcher = Dispatchers.Default
}
actual val applicationIoDispatcher: Dispatcher = IOSDispatcher()