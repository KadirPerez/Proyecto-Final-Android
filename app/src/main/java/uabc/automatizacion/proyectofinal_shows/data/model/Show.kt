package uabc.automatizacion.proyectofinal_shows.data.model

import com.squareup.moshi.Json
import uabc.automatizacion.proyectofinal_shows.data.database.ShowDatabase

data class SearchResponse(
    val score: Double,
    val show: Show
)

data class Show(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String?,
    @Json(name = "rating")
    val rating: Rating?,
    @Json(name = "image")
    val image: Image?,
    @Json(name = "genres")
    val genres: List<String>?,
    @Json(name = "summary")
    val summary: String?,
    @Json(name = "premiered")
    val premiered: String?,
    @Json(name = "ended")
    val ended: String?,
    @Json(name = "language")
    val language: String?,
    @Json(name = "network")
    val network: Network?,
    @Json(name = "officialSite")
    val officialSite: String?,
    @Json(name = "schedule")
    val schedule: Schedule?
)

data class Schedule(
    val time: String?,
    val days: List<String>?
)

data class Rating(
    val average: Double?
)

data class Network(
    val id: Int?,
    val name: String?,
    val country: Country?,
    val officialSite: String?
)

data class Country(
    val name: String?,
    val code: String?,
    val timezone: String?
)

data class Image(
    val medium: String,
    val original: String
)

fun Show.toShowDatabase(): ShowDatabase {
    return ShowDatabase(
        id = this.id,
        name = this.name,
        rating = this.rating,
        image = this.image,
        genres = this.genres,
        summary = this.summary,
        premiered = this.premiered,
        ended = this.ended,
        language = this.language,
        network = this.network,
        officialSite = this.officialSite,
        schedule = this.schedule
    )
}