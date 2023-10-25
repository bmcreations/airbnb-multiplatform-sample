package com.airbnb.sample.screens.login.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.utils.ui.Row

@Composable
internal fun LoginIdentifierEntry(
    isPhoneSelected: Boolean,
    region: String,
    onRegionTapped: () -> Unit,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x1),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().border(
                width = MaterialTheme.dimens.border,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
        ) {
            AnimatedContent(
                targetState = isPhoneSelected,
                transitionSpec = {
                    fadeIn() + slideInVertically() togetherWith slideOutVertically() + fadeOut()
                },
            ) {
                if (it) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clickable { onRegionTapped() }
                                .padding(
                                    horizontal = MaterialTheme.dimens.grid.x4,
                                    vertical = MaterialTheme.dimens.grid.x2
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Country/Region",
                                    color = MaterialTheme.colorScheme.onBackground.copy(ContentAlpha.medium),
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Text(region, style = MaterialTheme.typography.bodySmall)
                            }
                            Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null)
                        }
                        Divider(color = MaterialTheme.colorScheme.outline)
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = onValueChange,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    // TODO: transform numbers
                    visualTransformation = VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                if (value.text.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.grid.x4),
                        text = if (isPhoneSelected) "Phone Number" else "Email",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = ContentAlpha.medium),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        AnimatedContent(
            targetState = isPhoneSelected,
            transitionSpec = {
                fadeIn() + slideInVertically() togetherWith slideOutVertically() + fadeOut()
            },
        ) {
            if (it) {
                Text(
                    text = buildAnnotatedString {
                        append("We\'ll call or text you to confirm your number. Standard message and data rates apply. ")
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Privacy Policy")
                        }
                    },
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}