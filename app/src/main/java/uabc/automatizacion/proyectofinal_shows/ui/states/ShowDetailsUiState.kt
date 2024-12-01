package uabc.automatizacion.proyectofinal_shows.ui.states

import uabc.automatizacion.proyectofinal_shows.data.model.Show

sealed class ShowDetailsUiState {
    object Loading : ShowDetailsUiState()
    data class Success(val show: Show) : ShowDetailsUiState()
    data class Error(val message: String) : ShowDetailsUiState()
}