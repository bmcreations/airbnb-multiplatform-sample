package com.airbnb.sample.screens.web

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.sample.navigation.Screens
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState

@Composable
fun WebScreen(screenContent: Screens.Web) {
    val state = rememberWebViewState(screenContent.webUrl)

    WebView(
        modifier = Modifier.fillMaxSize(),
        state = state
    )
}