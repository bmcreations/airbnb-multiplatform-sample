package com.airbnb.sample.data.houses

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cabin
import androidx.compose.material.icons.rounded.Castle
import androidx.compose.material.icons.rounded.Houseboat
import androidx.compose.material.icons.rounded.Landscape
import androidx.compose.material.icons.rounded.Nature
import androidx.compose.material.icons.rounded.Rocket
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter

sealed interface HouseType {
    val label: String
    val descriptor: String
    @get:Composable
    val icon: Painter

    data object Cabin: HouseType {
        override val label: String
            get() = "Cabins"

        override val descriptor: String
            get() = "cabins"

        override val icon: Painter
            @Composable get() = rememberVectorPainter(Icons.Rounded.Cabin)
    }
    data object AwesomeView: HouseType {
        override val label: String
            get() = "Awesome Views"

        override val descriptor: String
            get() = "awesome views"

        override val icon: Painter
            @Composable get() = rememberVectorPainter(Icons.Rounded.Landscape)
    }

    data object Omg: HouseType {
        override val label: String
            get() = "OMG!"

        override val descriptor: String
            get() = "OMG! homes"

        override val icon: Painter
            @Composable get() = rememberVectorPainter(Icons.Rounded.Rocket)
    }

    data object Houseboat: HouseType {
        override val label: String
            get() = "Houseboats"

        override val descriptor: String
            get() = "houseboats"

        override val icon: Painter
            @Composable get() = rememberVectorPainter(Icons.Rounded.Houseboat)
    }

    data object Castle: HouseType {
        override val label: String
            get() = "Castles"

        override val descriptor: String
            get() = "castles"

        override val icon: Painter
            @Composable get() = rememberVectorPainter(Icons.Rounded.Castle)
    }

    companion object {
        val all = listOf(Cabin, AwesomeView, Omg, Houseboat, Castle)
    }
}