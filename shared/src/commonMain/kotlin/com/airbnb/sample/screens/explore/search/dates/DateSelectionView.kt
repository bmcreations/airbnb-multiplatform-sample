package com.airbnb.sample.screens.explore.search.dates

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.airbnb.sample.data.explore.FlexibleDateCriteria
import com.airbnb.sample.data.explore.SearchDateParameterType
import com.airbnb.sample.data.explore.SearchDateParameters
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.theme.outlineSecondaryVariant
import com.airbnb.sample.theme.secondaryText
import com.airbnb.sample.ui.components.FlatChip
import com.airbnb.sample.utils.ui.addIf
import com.kizitonwose.calendar.core.YearMonth
import kotlinx.datetime.DayOfWeek

@Composable
internal fun DateSelectionView(
    modifier: Modifier = Modifier,
    selectedType: SearchDateParameterType,
    parameters: SearchDateParameters?,
    onParametersChanged: (SearchDateParameters) -> Unit,
) {
    Box(modifier = modifier) {
        when (selectedType) {
            SearchDateParameterType.Dates -> {
                CalendarWithOffsetSelection(
                    modifier = Modifier.fillMaxSize(),
                    parameters = parameters,
                    onParametersChanged = onParametersChanged
                )
            }

            SearchDateParameterType.Months -> {

            }

            SearchDateParameterType.Flexible -> {
                FlexibleDateSelection(
                    modifier = Modifier.fillMaxSize(),
                    parameters = parameters,
                    onParametersChanged = onParametersChanged
                )
            }
        }
    }
}