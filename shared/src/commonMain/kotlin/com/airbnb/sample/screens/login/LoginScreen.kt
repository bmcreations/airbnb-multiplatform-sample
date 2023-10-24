package com.airbnb.sample.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.utils.screenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Screens.Login.Render() {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.statusBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.grid.x2)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Log in or sign up",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                Divider()
            }
        },
    ) { padding ->
//        LazyColumn(modifier = Modifier.fillMaxSize().then()) {  }
    }
}

@Composable
private fun Screens.Login.Content(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = screenViewModel()
) {
    val state by viewModel.stateFlow.collectAsState()

    Box(modifier = modifier) {

    }
}