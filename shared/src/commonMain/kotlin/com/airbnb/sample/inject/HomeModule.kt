package com.airbnb.sample.inject

import com.airbnb.sample.screens.home.HomeViewModel
import me.tatarka.inject.annotations.Component

@Component
abstract class HomeModule: AppModule() {
    abstract val viewModel: HomeViewModel
}