package com.airbnb.sample

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.airbnb.sample.inject.ApplicationComponent
import com.airbnb.sample.inject.LocalAppComponent
import com.airbnb.sample.inject.create

fun MainViewController() = ComposeUIViewController {
    val appComponent = ApplicationComponent::class.create()
    CompositionLocalProvider(LocalAppComponent provides appComponent) {
        App()
    }
}

