package com.airbnb.sample.screens.explore.search.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.airbnb.sample.data.explore.DateWindowOffset
import com.airbnb.sample.data.explore.SearchDateParameterType
import com.airbnb.sample.data.explore.SearchDateParameters
import com.airbnb.sample.screens.explore.search.dates.DateSelectionView
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.typography
import com.airbnb.sample.ui.components.SegmentedControl
import com.airbnb.sample.ui.components.VerticalScrollingCalendar
import com.airbnb.sample.utils.toLocalDate
import com.airbnb.sample.utils.ui.NoRippleInteractionSource
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.lighthousegames.logging.logging

@Composable
internal fun DateSelection(
    modifier: Modifier = Modifier,
    parameters: SearchDateParameters?,
    onParametersChanged: (SearchDateParameters) -> Unit,
    isActive: Boolean,
    onExpand: () -> Unit,
    onSkip: () -> Unit,
    onReset: () -> Unit,
    onNext: () -> Unit,
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
        onClick = if (!isActive) {
            { onExpand() }
        } else null,
    ) {
        Crossfade(targetState = isActive) { active ->
            if (active) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(top = MaterialTheme.dimens.inset),
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.inset),
                        text = "When's your trip?",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W700)
                    )

                    val dateSelectionTweaks = remember { SearchDateParameterType.entries }
                    var selectedTweak by remember {
                        mutableStateOf(parameters?.type ?: SearchDateParameterType.Dates)
                    }

                    SegmentedControl(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = MaterialTheme.dimens.inset)
                            .padding(vertical = MaterialTheme.dimens.inset),
                        options = dateSelectionTweaks,
                        selected = selectedTweak,
                        onOptionClicked = { selectedTweak = it },
                        titleForItem = { it.name }
                    )

                    DateSelectionView(
                        modifier = Modifier.weight(1f),
                        selectedType = selectedTweak,
                        parameters = parameters,
                        onParametersChanged = onParametersChanged
                    )

                    Divider(color = MaterialTheme.colorScheme.outline)

                    Footer(
                        modifier = Modifier.fillMaxWidth(),
                        hasChanges = parameters?.hasDateSelections() ?: false,
                        onSkip = onSkip,
                        onReset = onReset,
                        onNext = onNext
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
                    title = "When",
                    value = parameters?.printed() ?: "Any week",
                )
            }
        }
    }
}

@Composable
private fun Footer(
    modifier: Modifier = Modifier,
    hasChanges: Boolean,
    onSkip: () -> Unit,
    onReset: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = MaterialTheme.dimens.inset,
                vertical = MaterialTheme.dimens.staticGrid.x3,
            ).then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.clickable { if (hasChanges) onReset() else onSkip() },
            text = if (hasChanges) "Reset" else "Skip",
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W500)
        )

        Button(
            onClick = { onNext() },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
            ),
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.dimens.staticGrid.x4,
                    vertical = MaterialTheme.dimens.staticGrid.x2
                ),
                color = Color.White,
                text = "Next"
            )
        }
    }
}