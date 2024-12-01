package uabc.automatizacion.proyectofinal_shows.navigation

import kotlinx.serialization.Serializable

@Serializable
object ShowsList

@Serializable
data class ShowDetails(val showId: Int, val isFavourite: Boolean)

@Serializable
object Favourites