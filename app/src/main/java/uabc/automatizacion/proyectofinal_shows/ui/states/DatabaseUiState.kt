package uabc.automatizacion.proyectofinal_shows.ui.states

import uabc.automatizacion.proyectofinal_shows.data.database.ShowDatabase

sealed class DatabaseUiState {
    data object Loading : DatabaseUiState()
    data class Success(val favouritesShows: List<ShowDatabase>) : DatabaseUiState()
    data class Error(val message: String) : DatabaseUiState()
}