package com.airbnb.sample.ui.components

import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

@ExperimentalMaterialApi
@Composable
internal actual fun applyPlatformBackdropScaffoldStyle(state: BackdropScaffoldState) {
    val oldStyle = remember {
        UIApplication.sharedApplication.statusBarStyle
    }

    val light by remember {
        derivedStateOf {
            state.progress.from == BackdropValue.Revealed && state.progress.to == BackdropValue.Concealed && state.progress.fraction > .5f ||
                    state.progress.from == BackdropValue.Concealed && state.progress.to == BackdropValue.Revealed && state.progress.fraction < .5f ||
                    state.progress.from == BackdropValue.Concealed && state.progress.to == BackdropValue.Concealed
        }
    }

    DisposableEffect(light) {
        UIApplication.sharedApplication.setStatusBarStyle(
            if (light) UIStatusBarStyleLightContent else oldStyle,
            true
        )
        onDispose { UIApplication.sharedApplication.setStatusBarStyle(oldStyle, true) }
    }
}
