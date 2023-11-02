package com.airbnb.sample.inject

import com.airbnb.sample.utils.DefaultDispatchers
import com.airbnb.sample.utils.DispatcherProvider
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ScreenModelScope
abstract class DataComponent {
    @Provides
    fun providerDefaultDispatcher(): DispatcherProvider = DefaultDispatchers()
}