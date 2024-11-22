package uabc.automatizacion.proyectofinal_shows.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uabc.automatizacion.proyectofinal_shows.data.repository.ShowRepository
import uabc.automatizacion.proyectofinal_shows.ui.states.ShowsListUiState

class ShowsListViewModel : ViewModel() {

    private val _homeUIState = MutableStateFlow<ShowsListUiState>(ShowsListUiState.Loading)
    val showListUIState: StateFlow<ShowsListUiState> get() = _homeUIState

    fun fetchShows() {
        viewModelScope.launch {
            ShowRepository.getShows().collect { uiState ->
                _homeUIState.value = uiState
            }
        }
    }
}

