package com.airbnb.sample.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Accessibility
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.secondaryText
import com.airbnb.sample.ui.components.GradientButton
import com.airbnb.sample.ui.components.LargeHeader
import com.airbnb.sample.ui.components.SettingItem
import com.airbnb.sample.ui.components.SettingItemDefaults
import com.airbnb.sample.ui.resources.Drawables
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun Screens.Profile.Render() {
    Content(modifier = Modifier.fillMaxSize())
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {
    val navigator = LocalBottomSheetNavigator.current
    // TODO: support authenticated state
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.grid.x5)
    ) {
        item {
            Spacer(modifier = Modifier.padding(top = MaterialTheme.dimens.staticGrid.x14))
        }
        item {
            LargeHeader(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.inset),
                title = {
                    Text(text = "Profile")
                },
                description = {
                    Text(
                        text = "Log in to start planning your next trip.",
                    )
                }
            )
        }

        item {
            GradientButton(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.inset)
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.dimens.grid.x7),
                onClick = { navigator.show(Screens.LoginModal) },
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(
                    modifier = Modifier.padding(vertical = MaterialTheme.dimens.staticGrid.x1),
                    text = "Log in"
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.dimens.inset)
            ) {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = "Don\'t have an account?",
                    style = MaterialTheme.typography.labelSmall
                )
                ClickableText(
                    modifier = Modifier.alignByBaseline()
                        .padding(start = MaterialTheme.dimens.staticGrid.x1),
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.W600,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Sign up")
                        }
                    },
                    onClick = {
                        navigator.show(Screens.LoginModal)
                    }
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.inset)
                    .padding(top = MaterialTheme.dimens.grid.x7),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                onClick = {

                }
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = MaterialTheme.dimens.staticGrid.x3,
                    ),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x2)
                    ) {
                        Text(
                            text = "Airbnb your place",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = "It\'s simple to get set up and start earning.",
                            color = MaterialTheme.colorScheme.secondaryText,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(vertical = MaterialTheme.dimens.staticGrid.x2),
                        painter = painterResource("drawable/ic_profile_house_list.png"),
                        contentDescription = null
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(top = MaterialTheme.dimens.grid.x3)) {
                SettingItem(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.inset),
                    icon = Drawables.Settings,
                    title = "Settings"
                ) {
                    navigator.show(Screens.Settings)
                }

                SettingItem(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.inset),
                    icon = rememberVectorPainter(Icons.Rounded.Accessibility), // TODO: find an icon
                    title = "Accessibility"
                ) {

                }

                SettingItem(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.inset),
                    icon = Drawables.HelpCircle,
                    title = "Visit the Help Center",
                    showDivider = false,
                ) {

                }
            }
        }
    }
}