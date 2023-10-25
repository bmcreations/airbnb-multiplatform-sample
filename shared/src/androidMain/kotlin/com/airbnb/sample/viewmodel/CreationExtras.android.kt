package com.airbnb.sample.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras as AndroidXCreationExtras
import androidx.lifecycle.viewmodel.CreationExtras.Key as AndroidXCreationExtrasKey
import androidx.lifecycle.viewmodel.MutableCreationExtras as AndroidXMutableCreationExtras

actual typealias CreationExtras = AndroidXCreationExtras

actual typealias CreationExtrasKey<T> = AndroidXCreationExtrasKey<T>

actual typealias EmptyCreationExtras = AndroidXCreationExtras.Empty

actual typealias MutableCreationExtras = AndroidXMutableCreationExtras

/**
 * @see [ViewModelProvider.NewInstanceFactory.VIEW_MODEL_KEY]
 */
actual val VIEW_MODEL_KEY: Key<String> get() = ViewModelProvider.NewInstanceFactory.VIEW_MODEL_KEY