package com.airbnb.sample.data.explore

import com.kizitonwose.calendar.core.YearMonth
import kotlinx.datetime.Instant
import kotlinx.datetime.Month

data class SearchParameters(
    val location: String,
    val dateRange: ClosedRange<Instant>,
    val guests: AccompanyingGuests,
)

sealed interface DateWindowOffset {
    val label: String

    data object None : DateWindowOffset {
        override val label: String = "Exact dates"
    }

    data class Count(val count: Int) : DateWindowOffset {
        override val label: String = when (count) {
            1 -> "± 1 day"
            else -> "± $count days"
        }
    }
}

data class FlexibleDateCriteria(
    val length: StayLength,
    val months: List<YearMonth>
) {
    sealed interface StayLength {
        data object Weekend : StayLength
        data object Week : StayLength
        data object Month : StayLength
    }
}

data class AccompanyingGuests(
    val adults: Int,
    val children: Int,
    val infants: Int,
    val pets: Int,
) {

    fun hasSelections() = listOf(adults, children, infants, pets).any { it > 0 }

    fun addAdult(): AccompanyingGuests {
        return copy(adults = adults + 1)
    }

    fun removeAdult(): AccompanyingGuests {
        val newValue = (adults - 1).coerceAtLeast(0)
        return copy(
            adults = newValue,
            children = if (newValue == 0) 0 else children,
            infants = if (newValue == 0) 0 else infants,
            pets = if (newValue == 0) 0 else pets,
        )
    }

    fun addChild(): AccompanyingGuests {
        return copy(
            adults = if (adults == 0) 1 else adults,
            children = children + 1
        )
    }

    fun removeChild(): AccompanyingGuests {
        return copy(children = (children - 1).coerceAtLeast(0))
    }

    fun addInfant(): AccompanyingGuests {
        return copy(
            adults = if (adults == 0) 1 else adults,
            infants = infants + 1
        )
    }

    fun removeInfant(): AccompanyingGuests {
        return copy(infants = (infants - 1).coerceAtLeast(0))
    }

    fun addPet(): AccompanyingGuests {
        return copy(
            adults = if (adults == 0) 1 else adults,
            pets = pets + 1
        )
    }

    fun removePet(): AccompanyingGuests {
        return copy(pets = (pets - 1).coerceAtLeast(0))
    }

    override fun toString(): String {
        val guestCount = adults + children
        if (guestCount == 0) return ""

        val guestString = if (guestCount > 1) {
            "$guestCount guests"
        } else {
            "1 guest"
        }

        val infantString = if (infants > 0) {
            val count = infants
            if (count > 1) {
                ", $count infants"
            } else {
                ", 1 infant"
            }
        } else {
            ""
        }

        val petString = if (pets > 0) {
            val count = pets
            if (count > 1) {
                ", $count pets"
            } else {
                ", 1 pet"
            }
        } else {
            ""
        }

        return guestString + infantString + petString
    }

    companion object {
        val Empty = AccompanyingGuests(0, 0, 0, 0)
    }
}
