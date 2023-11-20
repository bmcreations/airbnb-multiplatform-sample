@file:Suppress("ClassName")

package com.airbnb.sample.data.explore

sealed interface SearchLocationOption {

    companion object {
        val choices = listOf(
            Flexible,
            PhysicalPlace.Europe,
            PhysicalPlace.Canada,
            PhysicalPlace.Caribbean,
            PhysicalPlace.Italy,
            PhysicalPlace.South_America,
            PhysicalPlace.United_Kingdom,
            PhysicalPlace.Mexico,
            PhysicalPlace.Spain
        )
    }

    data object Flexible : SearchLocationOption

    data class Custom(val place: String) : SearchLocationOption

    sealed interface PhysicalPlace {
        data object Europe : SearchLocationOption, PhysicalPlace
        data object Canada : SearchLocationOption, PhysicalPlace
        data object Caribbean : SearchLocationOption, PhysicalPlace
        data object Italy : SearchLocationOption, PhysicalPlace
        data object South_America : SearchLocationOption, PhysicalPlace
        data object United_Kingdom : SearchLocationOption, PhysicalPlace
        data object Mexico : SearchLocationOption, PhysicalPlace
        data object Spain : SearchLocationOption, PhysicalPlace

        fun named() = this::class.simpleName.orEmpty().replace("_", " ")
    }

    fun displayLabel() = when (this) {
        is SearchLocationOption.Custom -> place
        is SearchLocationOption.PhysicalPlace -> named()
        SearchLocationOption.Flexible -> "I'm flexible"
    }
}