package com.airbnb.sample.data.houses

import com.airbnb.sample.utils.format
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.days

sealed interface Stay {
    val city: String
    val province: String
    val country: String
    val usdPricePoint: Double
    val distanceFromUser: Int
    val nextAvailabilityDates: ClosedRange<Instant>
    val rating: Double
    val isFavorite: Boolean
    val type: HouseType

    fun displayLocation(currentlyLocatedIn: String) = if (currentlyLocatedIn == this.country) {
        "$city, $province"
    } else {
        "$city, ${this.country}"
    }

    fun stayLength(timezone: TimeZone = TimeZone.currentSystemDefault()) =
        nextAvailabilityDates.start.daysUntil(nextAvailabilityDates.endInclusive.minus(1.days), timezone)

    fun printedDuration() =
        "${nextAvailabilityDates.start.format("MMM dd")} - ${nextAvailabilityDates.endInclusive.format("MMM dd")}"

    fun totalPriceOfStay() = (stayLength() * usdPricePoint).roundToInt()

    data class Minimal(
        override val city: String,
        override val province: String,
        override val country: String,
        override val usdPricePoint: Double,
        override val distanceFromUser: Int,
        override val nextAvailabilityDates: ClosedRange<Instant>,
        override val rating: Double,
        val imageUrls: List<String>,
        override val isFavorite: Boolean,
        override val type: HouseType,
    ) : Stay
}

