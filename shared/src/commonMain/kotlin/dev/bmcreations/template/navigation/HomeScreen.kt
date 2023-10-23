package dev.bmcreations.template.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import dev.bmcreations.template.inject.HomeModule
import dev.bmcreations.template.inject.create
import dev.bmcreations.template.screens.home.HomeScreen
import dev.bmcreations.template.screens.home.HomeViewModel

object HomeScreen : Screen {

    // TODO: make a hiltViewModel() that caches instances
    private val viewModel: HomeViewModel = HomeModule::class.create().viewModel

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
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
            HomeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .then(Modifier.padding(padding)),
                viewModel = viewModel,
            )
        }
    }
}