package com.airbnb.sample.inject

import cafe.adriel.voyager.core.model.ScreenModel
import com.airbnb.sample.screens.home.HomeViewModel
import com.airbnb.sample.screens.login.LoginViewModel
import me.tatarka.inject.annotations.Component

@Component
abstract class ViewModelComponent: AppComponent() {
    abstract val loginViewModel: LoginViewModel
    abstract val homeViewModel: HomeViewModel

    inline fun <reified T: ScreenModel> getViewModel(): ScreenModel {
        return when (val clazz = T::class) {
            LoginViewModel::class -> loginViewModel
            HomeViewModel::class -> homeViewModel
            else -> throw NoSuchElementException("No viewModel found for ${clazz.simpleName}")
        }
    }
}