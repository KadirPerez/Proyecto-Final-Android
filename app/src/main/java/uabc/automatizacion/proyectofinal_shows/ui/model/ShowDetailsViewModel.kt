package uabc.automatizacion.proyectofinal_shows.ui.model

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uabc.automatizacion.proyectofinal_shows.data.model.Show
import uabc.automatizacion.proyectofinal_shows.data.repository.ShowRepository
import uabc.automatizacion.proyectofinal_shows.navigation.ShowDetails
import uabc.automatizacion.proyectofinal_shows.ui.states.ShowDetailsUiState
import uabc.automatizacion.proyectofinal_shows.ui.states.ShowsListUiState

class ShowDetailViewModel(
    safeStateHandle: SavedStateHandle,
): ViewModel() {
    val showId: Int = safeStateHandle.toRoute<ShowDetails>().showId
    val isFavourite: Boolean = safeStateHandle.toRoute<ShowDetails>().isFavourite

    private val _homeUIState = MutableStateFlow<ShowDetailsUiState>(ShowDetailsUiState.Loading)
    val showDetailUIState: StateFlow<ShowDetailsUiState> get() = _homeUIState

    init {
        fetchShowPerId(showId)
    }

    private fun fetchShowPerId(id:Int) {
        viewModelScope.launch {
            ShowRepository.getShowPerId(id).collect { uiState ->
                _homeUIState.value = uiState
            }
        }
    }
}