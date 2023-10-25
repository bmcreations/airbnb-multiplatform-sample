package com.airbnb.sample.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AndroidDispatcher : Dispatcher {
    override fun dispatcher(): CoroutineDispatcher = Dispatchers.IO
}

actual val applicationIoDispatcher: Dispatcher = AndroidDispatcher()