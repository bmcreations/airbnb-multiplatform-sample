package com.airbnb.sample.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.utils.screenViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Screens.Home.Render() {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text("Template")
                }
            )
        },
    ) { padding ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .then(Modifier.padding(padding)),
        )
    }
}

@Composable
private fun Screens.Home.Content(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = screenViewModel()
) {
    val state by viewModel.stateFlow.collectAsState()

    Box(modifier = modifier) {
        HomeScreen(state, viewModel::dispatchEvent)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun HomeScreen(
    state: HomeViewModel.State,
    dispatch: (HomeViewModel.Event) -> Unit,
) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                dispatch(HomeViewModel.Event.ShowImage(!state.showImage))
            }
        ) {
            Text("Hello, ${state.greeting}!")
        }
        AnimatedVisibility(state.showImage) {
            Image(
                painterResource("compose-multiplatform.xml"),
                null
            )
        }
    }
}