package com.airbnb.sample.domain.settings

import com.airbnb.sample.data.explore.SearchParameters
import com.airbnb.sample.data.houses.HouseType
import com.airbnb.sample.data.houses.Stay
import com.airbnb.sample.data.location.LatLong
import com.airbnb.sample.networking.Unsplash
import com.benasher44.uuid.uuid4
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration.Companion.days

@Inject
class ExploreRepository {

    private val priceRandom = Random(4)
    private val distanceRandom = Random(3)
    private val ratingRandom = Random(2)
    private val imageRandom = Random(8)

    private val searchQuery = MutableStateFlow<SearchParameters?>(null)

    fun searchFor(parameters: SearchParameters) {
        searchQuery.value = parameters
    }

    fun clearSearch() {
        searchQuery.value = null
    }

    suspend fun getAvailableStays(
        minimal: Boolean = true,
        countPerType: Int = 5
    ): Result<List<Stay>> {
        if (!minimal) return Result.failure(NotImplementedError())

        return Result.success(HouseType.all.flatMap { type ->
            (0 until countPerType).map {
                val location = locationGenerator(it, type)

                Stay.Minimal(
                    id = uuid4().toString(),
                    city = location.city,
                    province = location.province,
                    country = location.country,
                    rating = randomRating(ratingRandom),
                    usdPricePoint = randomPricePoint(priceRandom),
                    distanceFromUser = randomDistance(distanceRandom),
                    nextAvailabilityDates = randomDateRange(Random),
                    imageUrls = (0..5).map {
                        when (type) {
                            HouseType.AwesomeView -> Unsplash.randomImageUrl("house,front&${imageRandom.nextInt()}", searchQuery.value?.location)
                            HouseType.Cabin -> Unsplash.randomImageUrl("cabin,front&${imageRandom.nextInt()}", searchQuery.value?.location)
                            HouseType.Castle -> Unsplash.randomImageUrl("castle,front&${imageRandom.nextInt()}", searchQuery.value?.location)
                            HouseType.Houseboat -> Unsplash.randomImageUrl("boat,front&${imageRandom.nextInt()}", searchQuery.value?.location)
                            HouseType.Omg -> Unsplash.randomImageUrl("house,front&${imageRandom.nextInt()}", searchQuery.value?.location)
                        }
                    },
                    isFavorite = false,
                    type = type,
                    location = location.gps
                )
            }
        })
    }
}

private data class StayLocation(
    val city: String,
    val province: String,
    val country: String,
    val gps: LatLong,
)

// Sample data generators
private val locationGenerator: (Int, HouseType) -> StayLocation = { index, type ->
    val options = when (type) {
        HouseType.AwesomeView -> {
            listOf(
                StayLocation(
                    "Middlebury", "Indiana", "United States",
                    LatLong(41.675289, -85.706093)
                ),
                StayLocation(
                    "Marcellus", "Michigan", "United States",
                    LatLong(42.027931, -85.817398)
                ),
                StayLocation(
                    "White Pigeon", "Michigan", "United States",
                    LatLong(41.792641, -85.865768)
                ),
                StayLocation(
                    "Grass Lake Charter Township", "Michigan", "United States",
                    LatLong(43.723970, -85.461310)
                ),
                StayLocation(
                    "Tipton", "Michigan", "United States",
                    LatLong(42.024521, -84.083183)
                ),
            )
        }

        HouseType.Cabin -> {
            listOf(
                StayLocation(
                    "Coloma", "Michigan", "United States",
                    LatLong(42.1861494, -86.308356)
                ),
                StayLocation(
                    "Saugatuck", "Michigan", "United States",
                    LatLong(42.654930114746094, -86.20410919189453)
                ),
                StayLocation(
                    "White Pigeon", "Michigan", "United States",
                    LatLong(41.79817581176758, -85.6431884765625)
                ),
                StayLocation(
                    "Put-in-Bay", "Ohio", "United States",
                    LatLong(41.6518884, -82.8186814)
                ),
                StayLocation(
                    "Hudsonsville", "Michigan", "United States",
                    LatLong(42.86600112915039, -85.86275482177734)
                ),
            )
        }

        HouseType.Castle -> {
            listOf(
                StayLocation(
                    "Ballintuim", "United Kingdom", "United Kingdom",
                    LatLong(56.6782693, -3.4683069)
                ),
                StayLocation(
                    "Kilcogan", "Galway", "Ireland",
                    LatLong(53.208245, -8.8789732)
                ),
                StayLocation(
                    "Bree", "Wexford", "Ireland",
                    LatLong(52.4406886, -6.6227326),
                ),
                StayLocation(
                    "Cleveland", "Wisconsin", "United States",
                    LatLong(43.9140625, -87.75079345703125)
                ),
                StayLocation(
                    "Portpatrick", "Scotland", "United Kingdom",
                    LatLong(54.8418486, -5.1168367)
                ),
            )
        }

        HouseType.Houseboat -> {
            listOf(
                StayLocation(
                    "Melrose", "New York", "United States",
                    LatLong(40.8256703, -73.9152416)
                ),
                StayLocation(
                    "Swansboro", "North Carolina", "United States",
                    LatLong(34.68995666503906, -77.12251281738281)
                ),
                StayLocation(
                    "Oakwood", "Georgia", "United States",
                    LatLong(34.227602,-83.8843455)
                ),
                StayLocation(
                    "Sanford", "Florida", "United States",
                    LatLong(28.8117345,-81.2680223)
                ),
                StayLocation(
                    "Mallorytown", "Ontario", "Canada",
                    LatLong(44.479209899902344,-75.884765625)
                ),
            )
        }

        HouseType.Omg -> {
            listOf(
                StayLocation(
                    "Stege", "Denmark", "Denmark",
                    LatLong(54.9863794,12.2861344)
                ),
                StayLocation(
                    "Drimnin", "Scotland", "United Kingdom",
                    LatLong(56.61523,-5.983825)
                ),
                StayLocation(
                    "Pelkosenniemi", "Finland", "Finland",
                    LatLong(67.10891723632812,27.515344619750977)
                ),
                StayLocation(
                    "Lac-Beauport", "Quebec", "Canada",
                    LatLong(46.94734191894531,-71.29499816894531)
                ),
                StayLocation(
                    "Austin", "Texas", "United States",
                    LatLong(30.2711286,-97.7436995)
                ),
            )
        }
    }

    options[index]
}

private val randomPricePoint: (Random) -> Double = {
    it.nextDouble(56.23, 1_500.00)
}

private val randomRating: (Random) -> Double = {
    it.nextDouble(1.3, 5.0)
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