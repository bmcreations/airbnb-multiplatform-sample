package com.airbnb.sample.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.secondaryText
import com.airbnb.sample.ui.components.LargeHeader

@Composable
internal fun Screens.Profile.Render() {
    Content(modifier = Modifier.fillMaxSize())
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {
    val navigator = LocalBottomSheetNavigator.current
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.inset),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.grid.x4)
    ) {
        item {
            Spacer(modifier = Modifier.requiredHeight(MaterialTheme.dimens.staticGrid.x6))
        }
        item {
            LargeHeader(
                title = {
                    Text(text = "Profile", style = MaterialTheme.typography.titleMedium)
                },
                description = {
                    Text(
                        text = "Log in to start planning your next trip.",
                        color = MaterialTheme.colorScheme.secondaryText,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
        }

        item {
            Button(
                modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.dimens.grid.x3),
                onClick = { navigator.show(Screens.Login) },
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(
                    modifier = Modifier.padding(vertical = MaterialTheme.dimens.staticGrid.x1),
                    text = "Log in"
                )
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = "Don\'t have an account?",
                    color = MaterialTheme.colorScheme.onBackground.copy(ContentAlpha.medium),
                    style = MaterialTheme.typography.labelSmall
                )
                ClickableText(
                    modifier = Modifier.alignByBaseline().padding(start = MaterialTheme.dimens.staticGrid.x1),
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.W600,
                                color = MaterialTheme.colorScheme.onBackground,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Sign up")
                        }
                    },
                    onClick = {
                        navigator.show(Screens.Login)
                    }
                )
            }
        }
    }

}