package com.airbnb.sample.data.explore

sealed interface SearchQueryBuilderSection {
    data object Where: SearchQueryBuilderSection
    data object When: SearchQueryBuilderSection
    data object Who: SearchQueryBuilderSection
}