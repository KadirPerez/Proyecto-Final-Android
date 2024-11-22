package uabc.automatizacion.proyectofinal_shows.ui.states

import uabc.automatizacion.proyectofinal_shows.data.model.Show

sealed class ShowsListUiState {
    object Loading : ShowsListUiState()
    data class Success(val shows: List<Show>) : ShowsListUiState()
    data class Error(val message: String) : ShowsListUiState()
}