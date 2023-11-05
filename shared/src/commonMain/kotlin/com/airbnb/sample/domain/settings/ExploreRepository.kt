package com.airbnb.sample.domain.settings

import com.airbnb.sample.data.houses.HouseType
import com.airbnb.sample.data.houses.Stay
import com.airbnb.sample.utils.to
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import org.lighthousegames.logging.logging
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration.Companion.days

@Inject
class ExploreRepository {

    private val priceRandom = Random(4)
    private val distanceRandom = Random(3)
    private val ratingRandom = Random(2)
    private val imageRandom = Random(8)

    suspend fun getAvailableStays(minimal: Boolean = true, countPerType: Int = 5): Result<List<Stay>> {
        if (!minimal) return Result.failure(NotImplementedError())

        return Result.success(HouseType.all.flatMap { type ->
            (0 until countPerType).map {
                val location = locationGenerator(type)

                Stay.Minimal(
                    city = location.first,
                    province = location.second,
                    country = location.third,
                    rating = randomRating(ratingRandom),
                    usdPricePoint = randomPricePoint(priceRandom),
                    distanceFromUser = randomDistance(distanceRandom),
                    nextAvailabilityDates = randomDateRange(Random),
                    imageUrls = (0..5).map { "https://source.unsplash.com/random/?house,front&${imageRandom.nextInt()}" },
                    isFavorite = false,
                    type = type
                )
            }
        })
    }
}



private typealias City = String
private typealias Province = String
private typealias Country = String
private typealias StayLocation = Triple<City, Province, Country>

// Sample data generators
val locationGenerator: (HouseType) -> StayLocation = {
    val options = when (it) {
        HouseType.AwesomeView -> {
            listOf(
                "Middlebury" to "Indiana" to "United States",
                "Marcellus" to "Michigan" to "United States",
                "White Pigeon" to "Michigan" to "United States",
                "Grass Lake Charter Township" to "Michigan" to "United States",
                "Tipton" to "Michigan" to "United States",
            )
        }
        HouseType.Cabin -> {
            listOf(
                "Coloma" to "Michigan" to "United States",
                "Saugutuck" to "Michigan" to "United States",
                "White Pigeon" to "Michigan" to "United States",
                "Put-in-Bay" to "Ohio" to "United States",
                "Hudsonville" to "Michigan" to "United States",
            )
        }
        HouseType.Castle -> {
            listOf(
                "Ballintuim" to "United Kingdom" to "United Kingdom",
                "Kilcolgan" to "Galway" to "Ireland",
                "Bree" to "Wexford" to "Ireland",
                "Cleveland" to "Wisconsin" to "United States",
                "Portpatrick" to "Scotland" to "United Kingdom"
            )
        }
        HouseType.Houseboat -> {
            listOf(
                "Melrose" to "Ney York" to "United States",
                "Swansboro" to "North Carolina" to "United States",
                "Oakwood" to "Georgia" to "United States",
                "Sanford" to "Florida" to "United States",
                "Mallorytown" to "Ontario" to "Canada"
            )
        }
        HouseType.Omg -> {
            listOf(
                "Stege" to "Denmark" to "Denmark",
                "Drimnin" to "Scotland" to "United Kingdom",
                "Pelkosenniemi" to "Finland" to "Finland",
                "Lac-Beauport" to "Quebec" to "Canada",
                "Austin" to "Texas" to "United States"
            )
        }
    }

    options.random()
}

private val randomPricePoint: (Random) -> Double = {
    it.nextDouble(56.23, 1_500.00)
}

private val randomRating: (Random) -> Double = {
    it.nextDouble(0.0, 5.0)
}

private val randomDistance: (Random) -> Int = {
    it.nextInt(1..1_000)
}

private val randomDateRange: (Random) -> ClosedRange<Instant> = {
    val startOffset = it.nextInt(1..30)
    val duration = it.nextInt(2..14)

    val today = Clock.System.now()
    val startOfStay = today.plus(startOffset.days)
    val endOfStay = startOfStay.plus(duration.days)
    startOfStay.rangeTo(endOfStay)
}