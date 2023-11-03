package com.airbnb.sample.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.sample.data.login.SocialType
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.screens.login.components.LoginIdentifierEntry
import com.airbnb.sample.screens.login.components.loginSelectionDivider
import com.airbnb.sample.screens.login.components.socialLoginOptions
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.primarySecondaryToPrimaryTertiaryGradient
import com.airbnb.sample.ui.components.Modal
import com.airbnb.sample.ui.components.GradientButton
import com.airbnb.sample.viewmodel.screenViewModel

@Composable
internal fun Screens.LoginModal.RenderLogin() {
    Modal(
        title = key,
    ) {
        Screens.LoginModal.Content(
            modifier = Modifier,
        )
    }
}

@Composable
private fun Screens.LoginModal.Content(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = screenViewModel()
) {
    val state by viewModel.stateFlow.collectAsState()

    var textValue by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(state.textValue, textValue) {
        if (state.textValue != textValue.text) {
            viewModel.dispatchEvent(LoginViewModel.Event.OnTextValueUpdated(textValue.text))
        }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.inset),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.grid.x4)
    ) {
        item {
            Text(
                modifier = Modifier.padding(top = MaterialTheme.dimens.grid.x6),
                text = "Welcome to Airbnb",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        item {
            LoginIdentifierEntry(
                isPhoneSelected = state.isPhoneSelected,
                region = state.region,
                onRegionTapped = {

                },
                value = textValue,
                onValueChange = { textValue = it },
                errorMessage = state.textFieldErrorMessage
            )
        }
        item {
            GradientButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = state.hasMetRequirements,
                onClick = {
                    viewModel.dispatchEvent(LoginViewModel.Event.SubmitLogin)
                },
                shape = MaterialTheme.shapes.medium,
                brush = MaterialTheme.colorScheme.primarySecondaryToPrimaryTertiaryGradient,
            ) {
                Text(
                    modifier = Modifier.padding(vertical = MaterialTheme.dimens.staticGrid.x1),
                    text = "Continue"
                )
            }
        }
        loginSelectionDivider()
        socialLoginOptions(state.socialTypes) {
            when (it) {
                is SocialType.PhoneOrEmail -> {
                    if (it.isPhoneSelected) {
                        viewModel.dispatchEvent(LoginViewModel.Event.SwitchToEmail)
                    } else {
                        viewModel.dispatchEvent(LoginViewModel.Event.SwitchToPhone)
                    }
                }

                else -> Unit
            }
        }
        item {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = MaterialTheme.dimens.staticGrid.x2),
                textAlign = TextAlign.Center,
                text = "Need help?",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W600),
            )
        }

        item {
            Spacer(modifier = Modifier.requiredHeight(16.dp))
        }
    }
}