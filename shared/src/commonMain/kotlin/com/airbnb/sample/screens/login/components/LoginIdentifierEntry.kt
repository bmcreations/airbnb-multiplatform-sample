package com.airbnb.sample.screens.login.components

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.Row
import com.airbnb.sample.utils.ui.addIf

@Composable
internal fun LoginIdentifierEntry(
    isPhoneSelected: Boolean,
    region: String,
    onRegionTapped: () -> Unit,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    errorMessage: String? = null,
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
            var hasFocus by remember {
                mutableStateOf(false)
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .addIf(hasFocus) {
                        Modifier.border(
                            MaterialTheme.dimens.thickBorder,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = if (!isPhoneSelected)
                                MaterialTheme.shapes.medium
                            else MaterialTheme.shapes.medium.copy(
                                topStart = ZeroCornerSize,
                                topEnd = ZeroCornerSize,
                            )
                        )
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth().onFocusChanged { hasFocus = it.hasFocus },
                    value = value,
                    onValueChange = onValueChange,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    // TODO: transform numbers
                    visualTransformation = VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (isPhoneSelected) KeyboardType.Phone else KeyboardType.Email
                    )
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
            targetState = errorMessage,
            transitionSpec = {
                fadeIn() + slideInVertically() togetherWith slideOutVertically() + fadeOut()
            },
        ) {
            if (it != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(MaterialTheme.dimens.staticGrid.x3),
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        modifier = Modifier.padding(start = MaterialTheme.dimens.staticGrid.x1),
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.W600
                        )
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
                    text = "We\'ll call or text to confirm your number. Standard message and data rates apply.",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}