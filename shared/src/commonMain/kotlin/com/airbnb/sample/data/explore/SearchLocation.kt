@file:Suppress("ClassName")

package com.airbnb.sample.data.explore

sealed interface SearchLocationOption {

    val imageUrl: String?

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

    data object Flexible : SearchLocationOption {
        override val imageUrl: String = "https://images.unsplash.com/photo-1613235795113-e2c223afc08c?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8d29ybGQtbWFwfHx8fHx8MTcwMDYwMDYyOA&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080"
    }

    data class Custom(val place: String) : SearchLocationOption {
        override val imageUrl: String? = null
    }

    sealed interface PhysicalPlace {
        data object Europe : SearchLocationOption, PhysicalPlace {
            override val imageUrl: String = "https://images.unsplash.com/photo-1576185850227-1f72b7f8d483?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8ZXVyb3BlLW1hcHx8fHx8fDE3MDA2MDA3MTc&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080"
        }
        data object Canada : SearchLocationOption, PhysicalPlace {
            override val imageUrl: String = "https://images.unsplash.com/photo-1578924825042-31d14cf13c35?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2FuYWRhLW1hcHx8fHx8fDE3MDA2MDA3ODg&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080"
        }
        data object Caribbean : SearchLocationOption, PhysicalPlace {
            override val imageUrl: String = "https://images.unsplash.com/photo-1625239622428-ba0ae330a1f9?q=80&w=2826&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        }
        data object Italy : SearchLocationOption, PhysicalPlace {
            override val imageUrl: String = "https://images.unsplash.com/photo-1553290322-0440fe3b1ddd?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        }
        data object South_America : SearchLocationOption, PhysicalPlace {
            override val imageUrl: String = "https://images.unsplash.com/photo-1544906243-a69767cc000b?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        }
        data object United_Kingdom : SearchLocationOption, PhysicalPlace {
            override val imageUrl: String = "https://images.unsplash.com/photo-1662578546948-a30badf89995?q=80&w=2672&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        }
        data object Mexico : SearchLocationOption, PhysicalPlace {
            override val imageUrl: String = "https://images.unsplash.com/photo-1553290322-0440fe3b1ddd?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        }
        data object Spain : SearchLocationOption, PhysicalPlace {
            override val imageUrl: String = "https://images.unsplash.com/photo-1522072176817-41673f7f0ccc?q=80&w=2819&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        }

        fun named() = this::class.simpleName.orEmpty().replace("_", " ")
    }

    fun displayLabel() = when (this) {
        is Custom -> place
        is PhysicalPlace -> named()
        Flexible -> "I'm flexible"
    }
}