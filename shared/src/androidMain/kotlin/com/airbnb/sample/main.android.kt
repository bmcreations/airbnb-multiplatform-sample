package com.airbnb.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.airbnb.sample.inject.ApplicationComponent
import com.airbnb.sample.inject.LocalAppComponent
import com.airbnb.sample.inject.create

@Composable
fun MainView() {
    val appComponent = ApplicationComponent::class.create(LocalContext.current)
    CompositionLocalProvider(LocalAppComponent provides appComponent) {
        App()
    }
}