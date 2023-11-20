package com.airbnb.sample.screens.explore.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import com.airbnb.sample.data.explore.SearchQueryBuilderSection
import com.airbnb.sample.screens.LocalElevator
import com.airbnb.sample.screens.explore.search.components.BottomBar
import com.airbnb.sample.screens.explore.search.components.DateSelection
import com.airbnb.sample.screens.explore.search.components.OccupantSelection
import com.airbnb.sample.screens.explore.search.components.TripTypeSelector
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.utils.ui.addIf
import com.airbnb.sample.utils.ui.navigationBars
import com.skydoves.orbital.Orbital
import com.skydoves.orbital.OrbitalScope
import kotlinx.coroutines.delay

@Composable
internal fun SearchQueryBuilder(
    modifier: Modifier = Modifier,
    state: SearchQueryBuilderViewModel.State,
    dispatch: (SearchQueryBuilderViewModel.Event) -> Unit,
    onClose: () -> Unit,
    animatedContainer: @Composable OrbitalScope.() -> Unit,
) {
    var animateIn by remember { mutableStateOf(false) }
    var animateInBottomBar by remember {
        mutableStateOf(false)
    }

    val bottomBarShouldShow by remember(state.currentSection) {
        derivedStateOf {
            state.currentSection != null && state.currentSection != SearchQueryBuilderSection.When
        }
    }

    val elevator = LocalElevator.current
    LaunchedEffect(state.currentSection) {
        elevator.invoke(if (state.currentSection == SearchQueryBuilderSection.Where) 8.dp else 4.dp)
    }

    val closeAndReset = {
        onClose()
        dispatch(SearchQueryBuilderViewModel.Event.Reset)
    }

    Orbital(modifier = modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(
                                top = MaterialTheme.dimens.inset,
                                start = MaterialTheme.dimens.staticGrid.x4
                            )
                            .clip(CircleShape)
                            .clickable { closeAndReset() }
                            .border(
                                width = MaterialTheme.dimens.border,
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.outline
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.padding(MaterialTheme.dimens.staticGrid.x2),
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close"
                        )
                    }
                    TripTypeSelector(modifier = Modifier.weight(1f), selectedType = state.type) {
                        dispatch(SearchQueryBuilderViewModel.Event.OnTypeChanged(it))
                    }
                }
            },
            bottomBar = {
                AnimatedContent(
                    targetState = animateInBottomBar && bottomBarShouldShow,
                    transitionSpec = {
                        fadeIn(tween(delayMillis = 400)) + slideInVertically(tween(delayMillis = 400)) { it } togetherWith slideOutVertically { it } + fadeOut()
                    }
                ) { show ->
                    if (show) {
                        BottomBar(
                            onClear = { closeAndReset() }, onSearch = {})
                    }
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.inset)) {
                    animatedContainer()
                    AnimatedContent(
                        targetState = animateIn,
                        transitionSpec = {
                            slideInVertically(tween(delayMillis = 300)) + fadeIn(
                                tween(delayMillis = 300)
                            ) togetherWith fadeOut()
                        }
                    ) { show ->
                        if (show) {
                            val isActive = state.currentSection == SearchQueryBuilderSection.When
                            DateSelection(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .addIf(isActive) {
                                        Modifier
                                            .padding(WindowInsets.navigationBars.asPaddingValues())
                                            .padding(bottom = MaterialTheme.dimens.staticGrid.x1)
                                            .weight(1f)
                                    }
                                    .padding(horizontal = MaterialTheme.dimens.inset),
                                parameters = state.dateParameters,
                                onParametersChanged = {
                                    dispatch(SearchQueryBuilderViewModel.Event.OnDateParametersChanged(it))
                                },
                                isActive = state.currentSection == SearchQueryBuilderSection.When,
                                onExpand = {
                                    dispatch(
                                        SearchQueryBuilderViewModel.Event.OnSectionClicked(
                                            SearchQueryBuilderSection.When
                                        )
                                    )
                                },
                                onSkip = {},
                                onNext = {}
                            )
                        }
                    }

                    AnimatedContent(
                        targetState = animateIn,
                        transitionSpec = {
                            slideInVertically(tween(delayMillis = 450)) + fadeIn(tween(delayMillis = 300)) togetherWith fadeOut()
                        }
                    ) { show ->
                        if (show) {
                            OccupantSelection(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.dimens.inset),
                                guests = state.guests,
                                onGuestsChanged = {
                                    dispatch(
                                        SearchQueryBuilderViewModel.Event.OnGuestsChanged(
                                            it
                                        )
                                    )
                                },
                                isActive = state.currentSection == SearchQueryBuilderSection.Who,
                                onExpand = {
                                    dispatch(
                                        SearchQueryBuilderViewModel.Event.OnSectionClicked(
                                            SearchQueryBuilderSection.Who
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

            LaunchedEffect(Unit) {
                delay(50)
                animateIn = true
                animateInBottomBar = true
            }
        }
    }
}