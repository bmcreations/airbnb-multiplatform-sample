package com.airbnb.sample.screens.explore.search.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.airbnb.sample.data.explore.AccompanyingGuests
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.outlineSecondaryVariant
import com.airbnb.sample.theme.secondaryText
import com.airbnb.sample.ui.components.ToggleCounter
import com.airbnb.sample.utils.ui.NoRippleInteractionSource

@Composable
internal fun OccupantSelection(
    modifier: Modifier = Modifier,
    guests: AccompanyingGuests,
    onGuestsChanged: (AccompanyingGuests) -> Unit,
    isActive: Boolean,
    onExpand: () -> Unit,
) {
    val interactionSource by remember(isActive) {
        derivedStateOf {
            if (isActive) NoRippleInteractionSource()
            else MutableInteractionSource()
        }
    }

    ExpandableContent(
        modifier = modifier,
        isActive = isActive,
        interactionSource = interactionSource,
        onClick = { onExpand() }
    ) {
        Crossfade(targetState = isActive) { active ->
            if (active) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.dimens.inset,
                            vertical = MaterialTheme.dimens.inset
                        ),
                ) {
                    Text(
                        text = "Who's coming?",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W700)
                    )

                    LabelledToggleCounter(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = MaterialTheme.dimens.inset),
                        title = "Adults",
                        description = AnnotatedString("Ages 13 or above"),
                        count = guests.adults,
                        onDecrement = { onGuestsChanged(guests.removeAdult()) },
                        onIncrement = { onGuestsChanged(guests.addAdult()) }
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = MaterialTheme.dimens.inset),
                        color = MaterialTheme.colorScheme.outlineSecondaryVariant
                    )
                    LabelledToggleCounter(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Children",
                        description = AnnotatedString("Ages 2-12"),
                        count = guests.children,
                        onDecrement = { onGuestsChanged(guests.removeChild()) },
                        onIncrement = { onGuestsChanged(guests.addChild()) }
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = MaterialTheme.dimens.inset),
                        color = MaterialTheme.colorScheme.outlineSecondaryVariant
                    )
                    LabelledToggleCounter(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Infants",
                        description = AnnotatedString("Under 2"),
                        count = guests.infants,
                        onDecrement = { onGuestsChanged(guests.removeInfant()) },
                        onIncrement = { onGuestsChanged(guests.addInfant()) }
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = MaterialTheme.dimens.inset),
                        color = MaterialTheme.colorScheme.outlineSecondaryVariant
                    )
                    LabelledToggleCounter(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Pets",
                        description = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("Bringing a service animal?")
                            }
                        },
                        count = guests.pets,
                        onDecrement = { onGuestsChanged(guests.removePet()) },
                        onIncrement = { onGuestsChanged(guests.addPet()) }
                    )
                }
            } else {
                CollapsedContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.dimens.inset,
                            vertical = MaterialTheme.dimens.staticGrid.x6,
                        ),
                    title = "Who",
                    value = guests.toString().takeIf { it.isNotEmpty() } ?: "Add guests",
                )
            }
        }
    }
}

@Composable
private fun LabelledToggleCounter(
    modifier: Modifier = Modifier,
    title: String,
    description: AnnotatedString,
    count: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.staticGrid.x2)
        ) {
            Text(title, style = MaterialTheme.typography.bodySmall)
            Text(
                description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondaryText
            )
        }
        Column(
            modifier = Modifier.weight(1f),
        ) {
            ToggleCounter(
                modifier = Modifier.align(Alignment.End),
                count = count,
                onDecrement = onDecrement,
                onIncrement = onIncrement,
            )
        }
    }
}