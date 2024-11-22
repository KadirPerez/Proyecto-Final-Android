package uabc.automatizacion.proyectofinal_shows.data.model

data class Show(
    val id: Int,
    val name: String,
    val rating: Rating,
    val image: Image?
)

data class Schedule(
    val time: String,
    val days: List<String>
)

data class Rating(
    val average: Double?
)

data class Network(
    val id: Int,
    val name: String,
    val country: Country,
    val officialSite: String?
)

data class Country(
    val name: String,
    val code: String,
    val timezone: String
)

data class Externals(
    val tvrage: Int?,
    val thetvdb: Int?,
    val imdb: String?
)

data class Image(
    val medium: String,
    val original: String
)

data class Links(
    val self: Link,
    val previousepisode: Link?
)

data class Link(
    val href: String
)