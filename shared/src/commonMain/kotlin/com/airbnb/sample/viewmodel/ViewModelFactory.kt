package com.airbnb.sample.viewmodel

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.airbnb.sample.inject.ApplicationComponent
import com.airbnb.sample.inject.LocalAppComponent
import com.airbnb.sample.inject.ViewModelComponent
import com.airbnb.sample.inject.create
import kotlin.reflect.KClass

/**
 * Implementations of `Factory` interface are responsible to instantiate [ViewModel]s.
 */
interface ViewModelFactory<VM : ScreenModel> {
    val viewModelClass: KClass<VM>
    fun create(extras: CreationExtras): VM
}

/**
 * Creates a [ViewModelFactory] that returns the result of invoking [builder].
 */
inline fun <reified VM : ScreenModel> viewModelFactory(
    crossinline builder: CreationExtras.() -> VM,
): ViewModelFactory<VM> = object : ViewModelFactory<VM> {
    override val viewModelClass: KClass<VM> = VM::class
    override fun create(extras: CreationExtras): VM = builder(extras)
}

@Composable
inline fun <reified VM : ScreenModel> Screen.screenViewModel(
    applicationComponent: ApplicationComponent = LocalAppComponent.current,
    factory: ViewModelFactory<VM> = viewModelFactory {
        with(ViewModelComponent::class.create(applicationComponent)) {
            getViewModel<VM>()
        } as VM
    }
): VM {
    val key = factory.viewModelClass.qualifiedName
    return rememberScreenModel(tag = key) { factory.create(EmptyCreationExtras) }
}