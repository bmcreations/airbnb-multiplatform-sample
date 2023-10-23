package dev.bmcreations.template.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.lighthousegames.logging.logging

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
) {
    val state by viewModel.stateFlow.collectAsState()

    Box(modifier = modifier) {
        HomeScreen(state, viewModel::dispatchEvent)
    }

    LaunchedEffect(state) {
        logging().d { "state(${state.hashCode()})=$state" }
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