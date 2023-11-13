package com.airbnb.sample.screens.explore

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.sample.navigation.LocalPlatformNavigator
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.screens.explore.components.ListContent
import com.airbnb.sample.screens.explore.components.MapContent
import com.airbnb.sample.screens.explore.components.MapFab
import com.airbnb.sample.screens.explore.components.TopBar
import com.airbnb.sample.screens.explore.components.TotalToggle
import com.airbnb.sample.screens.main.LocalBottomNavAnimator
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.viewmodel.screenViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
internal fun Screens.Main.Explore.RenderExplore() {
    Content(modifier = Modifier.fillMaxSize())
}

@Composable
private fun Screens.Main.Explore.Content(
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = screenViewModel()
) {
    val state by viewModel.stateFlow.collectAsState()
    ExploreScreen(modifier = modifier, state = state, dispatch = viewModel::dispatchEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreScreen(
    modifier: Modifier = Modifier,
    state: ExploreViewModel.State,
    dispatch: (ExploreViewModel.Event) -> Unit,
) {
    BoxWithConstraints {
        val maxHeight = this.maxHeight
        val density = LocalDensity.current
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalPlatformNavigator.current

        var topBarHeight by remember { mutableStateOf(0.dp) }
        var decorLabelHeight by remember { mutableStateOf(0.dp) }
        val peekHeight by remember(decorLabelHeight) {
            derivedStateOf {
                56.dp +  // topbar height
                    decorLabelHeight
            }
        }

        val sheetMaxHeight by remember(maxHeight, topBarHeight) {
            derivedStateOf { maxHeight - topBarHeight }
        }

        val bottomScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberStandardBottomSheetState(
                SheetValue.Expanded,
                skipHiddenState = false,
                confirmValueChange = { value -> value != SheetValue.Hidden }
            ),
        )

        var fabAlpha by remember {
            mutableFloatStateOf(1f)
        }

        val offsetProvider = LocalBottomNavAnimator.current
        LaunchedEffect(bottomScaffoldState.bottomSheetState) {
            snapshotFlow { runCatching { bottomScaffoldState.bottomSheetState.requireOffset() } }
                .map {
                    it.map { with(density) { it.toDp() } }
                        .map { offset -> sheetMaxHeight - offset }
                        .getOrDefault(sheetMaxHeight)
                }
                .map { (it / (sheetMaxHeight * 0.90f)).coerceIn(0f, 1f) }
                .onEach {
                    fabAlpha = it
                    offsetProvider(it)
                }
                .launchIn(this)
        }

        DisposableEffect(Unit) {
            onDispose { offsetProvider(1f) }
        }

        Scaffold(
            modifier = modifier,
            topBar = { TopBar(state, dispatch, onSizeDetermined = { topBarHeight = it }) },
            floatingActionButton = {
                MapFab(
                    modifier = Modifier
                        .padding(bottom = MaterialTheme.dimens.staticGrid.x3),
                    alphaProvider = { fabAlpha }
                ) {
                    coroutineScope.launch {
                        bottomScaffoldState.bottomSheetState.partialExpand()
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) {

            val filteredResults by remember(state.results, state.selectedHouseType) {
                derivedStateOf { state.results.filter { it.type == state.selectedHouseType } }
            }

            BottomSheetScaffold(
                scaffoldState = bottomScaffoldState,
                sheetShape = MaterialTheme.shapes.large.copy(
                    bottomStart = ZeroCornerSize,
                    bottomEnd = ZeroCornerSize
                ),
                sheetTonalElevation = 0.dp,
                sheetShadowElevation = 4.dp,
                sheetContainerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onSurface,
                sheetContentColor = MaterialTheme.colorScheme.onSurface,
                sheetPeekHeight = peekHeight,
                sheetContent = {
                    val showTotalSelector by remember(state.selectedHouseType, state.houseTypes) {
                        derivedStateOf {
                            state.selectedHouseType == state.houseTypes.firstOrNull()
                        }
                    }

                    val typeDisplayed by remember(filteredResults) {
                        derivedStateOf { filteredResults.firstOrNull()?.type }
                    }

                    Text(
                        modifier = Modifier.fillMaxWidth().onPlaced {
                            with(density) { decorLabelHeight = it.size.height.toDp() }
                        },
                        textAlign = TextAlign.Center,
                        text = "${filteredResults.count()} ${typeDisplayed?.descriptor}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W600)
                    )

                    ListContent(
                        modifier = Modifier.height(maxHeight - topBarHeight),
                        results = filteredResults,
                        showTotalSelector = showTotalSelector,
                        totalPriceSelected = state.useTotal,
                        onFavoriteClick = {
                            // TODO: handle authenticated state
                            navigator.show(Screens.LoginModal)
                        },
                        totalToggle = {
                            TotalToggle(checked = state.useTotal) {
                                dispatch(
                                    ExploreViewModel.Event.OnShowTotalChanged(it)
                                )
                            }
                        },
                        viewListing = {

                        }
                    )
                },
                content = {
                    MapContent(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = topBarHeight,
                            bottom = peekHeight,
                        ),
                        useTotalPrice = state.useTotal,
                        mapSettings = state.mapSettings,
                        results = filteredResults
                    ) {

                    }
                },
            )
        }
    }
}