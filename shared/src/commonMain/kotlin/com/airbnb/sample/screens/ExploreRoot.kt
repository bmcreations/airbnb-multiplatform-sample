package com.airbnb.sample.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.sample.data.explore.SearchQueryBuilderSection
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.screens.explore.experience.ExploreScreen
import com.airbnb.sample.screens.explore.experience.ExploreViewModel
import com.airbnb.sample.screens.explore.experience.components.SearchBar
import com.airbnb.sample.screens.explore.search.SearchQueryBuilder
import com.airbnb.sample.screens.explore.search.SearchQueryBuilderViewModel
import com.airbnb.sample.screens.explore.search.components.LocationSelection
import com.airbnb.sample.screens.main.LocalBottomNavAnimator
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.secondaryText
import com.airbnb.sample.utils.ui.NoRippleInteractionSource
import com.airbnb.sample.viewmodel.screenViewModel
import com.skydoves.orbital.animateBounds
import com.skydoves.orbital.rememberContentWithOrbitalScope
import com.skydoves.orbital.rememberMovableContentOf
import org.lighthousegames.logging.logging

@Composable
internal fun Screens.Main.Explore.RenderExplore() {
    Content(modifier = Modifier.fillMaxSize())
}

internal enum class ScreenContent {
    Explore, Search
}

val LocalElevator: ProvidableCompositionLocal<(Dp) -> Unit> =
    staticCompositionLocalOf { error("LocalElevator not initialized") }

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    exploreViewModel: ExploreViewModel = Screens.Main.Explore.screenViewModel(),
    searchViewModel: SearchQueryBuilderViewModel = Screens.Main.Explore.screenViewModel()
) {
    var screenContent: ScreenContent by remember {
        mutableStateOf(ScreenContent.Explore)
    }

    val exploreState by exploreViewModel.stateFlow.collectAsState()
    val searchState by searchViewModel.stateFlow.collectAsState()

    val content = rememberMovableContentOf(screenContent) {
        when (screenContent) {
            ScreenContent.Explore -> ExploreSearchBar()

            ScreenContent.Search -> LocationSelection(
                modifier = Modifier.fillMaxWidth(),
                locationOption = searchState.where,
                isActive = searchState.currentSection == SearchQueryBuilderSection.Where,
                onLocationOptionSelected = {
                    searchViewModel.dispatchEvent(
                        SearchQueryBuilderViewModel.Event.OnLocationOptionSelected(it)
                    )
                },
                onSearchClicked = {

                }
            )
        }
    }

    var contentElevation by remember(screenContent) {
        mutableStateOf(10.dp)
    }

    val animatedContainer = rememberContentWithOrbitalScope {
        AnimatedContainer(
            modifier = Modifier.animateBounds(
                when (screenContent) {
                    ScreenContent.Explore -> Modifier
                        .padding(top = 0.5.dp)

                    ScreenContent.Search -> Modifier
                        .padding(top = MaterialTheme.dimens.staticGrid.x7)
                        .padding(horizontal = MaterialTheme.dimens.inset)
                }
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = when (screenContent) {
                    ScreenContent.Explore -> 10.dp
                    ScreenContent.Search -> contentElevation
                }
            ),
            interactionSource = remember { NoRippleInteractionSource() },
            shape = when (screenContent) {
                ScreenContent.Explore -> CircleShape
                ScreenContent.Search -> MaterialTheme.shapes.large
            },
            onClick = {
                if (screenContent == ScreenContent.Explore) {
                    screenContent = ScreenContent.Search
                } else {
                    searchViewModel.dispatchEvent(
                        SearchQueryBuilderViewModel.Event.OnSectionClicked(
                            SearchQueryBuilderSection.Where
                        )
                    )
                }
            },
            content = content
        )
    }


    val offsetProvider = LocalBottomNavAnimator.current
    var realOffset by remember {
        mutableStateOf(1f)
    }
    val coercedOffset by animateFloatAsState(
        if (screenContent == ScreenContent.Search) 0f else realOffset
    )

    LaunchedEffect(coercedOffset) {
        offsetProvider(coercedOffset)
    }

    when (screenContent) {
        ScreenContent.Explore -> {
            ExploreScreen(
                modifier = modifier,
                state = exploreState,
                dispatch = { exploreViewModel.dispatchEvent(it) },
                offsetProvider = { realOffset = it },
                animatedContainer = animatedContainer,
            )
        }
        ScreenContent.Search -> {
            CompositionLocalProvider(LocalElevator provides { contentElevation = it }) {
                SearchQueryBuilder(
                    modifier = modifier,
                    state = searchState,
                    dispatch = { searchViewModel.dispatchEvent(it) },
                    animatedContainer = animatedContainer,
                    onClose = {
                        screenContent = ScreenContent.Explore
                    }
                )
            }
        }
    }
}

@Composable
private fun ExploreSearchBar() {
    SearchBar(
        title = {
            Text(
                text = "Where to?",
                style = MaterialTheme.typography.labelSmall
            )
        },
        subtitle = {
            Text(
                "Anywhere • Any week • Add guests",
                style = MaterialTheme.typography.labelSmall
                    .copy(
                        color = MaterialTheme.colorScheme.secondaryText,
                        fontWeight = FontWeight.W400,
                        fontSize = 10.sp
                    )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedContainer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = CircleShape,
    border: BorderStroke = BorderStroke(0.5.dp, Color(0xFFDCDCDC).copy(alpha = 0.72f)),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: CardElevation = CardDefaults.cardElevation(
        defaultElevation = 10.dp
    ),
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource,
        onClick = onClick,
    ) {
        content()
    }
}