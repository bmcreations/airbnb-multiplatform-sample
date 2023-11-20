package com.airbnb.sample.data.houses

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.airbnb.sample.data.location.LatLong
import com.airbnb.sample.utils.format
import com.airbnb.sample.utils.formatAsMoney
import com.airbnb.sample.utils.printed
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.days

sealed interface Stay {
    val id: String
    val city: String
    val province: String
    val country: String
    val usdPricePoint: Double
    val distanceFromUser: Int
    val nextAvailabilityDates: ClosedRange<Instant>
    val rating: Double
    val isFavorite: Boolean
    val type: HouseType
    val location: LatLong

    fun displayLocation(currentlyLocatedIn: String) = if (currentlyLocatedIn == this.country) {
        "$city, $province"
    } else {
        "$city, ${this.country}"
    }

    fun stayLength(timezone: TimeZone = TimeZone.currentSystemDefault()) =
        nextAvailabilityDates.start.daysUntil(nextAvailabilityDates.endInclusive.minus(1.days), timezone)

    fun printedDuration() = nextAvailabilityDates.printed()

    fun totalPriceOfStay() = (stayLength() * usdPricePoint).roundToInt()

    data class Minimal(
        override val id: String,
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
        override val location: LatLong,
    ) : Stay
}

