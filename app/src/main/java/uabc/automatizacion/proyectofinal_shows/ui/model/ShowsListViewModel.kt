package uabc.automatizacion.proyectofinal_shows.ui.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import uabc.automatizacion.proyectofinal_shows.data.database.DaoShows
import uabc.automatizacion.proyectofinal_shows.data.database.ShowDatabase
import uabc.automatizacion.proyectofinal_shows.data.database.toShow
import uabc.automatizacion.proyectofinal_shows.data.model.Show
import uabc.automatizacion.proyectofinal_shows.data.model.toShowDatabase
import uabc.automatizacion.proyectofinal_shows.data.repository.ShowRepository
import uabc.automatizacion.proyectofinal_shows.ui.states.DatabaseUiState
import uabc.automatizacion.proyectofinal_shows.ui.states.ShowsListUiState
import javax.inject.Inject

data class AppState(
    var title: String = "",
    var searchText: String = "",

    var isHomeSelected: Boolean = true,
    var isFavoriteSelected: Boolean = false,
    var isInDetails: Boolean = false
)

@HiltViewModel
class ShowsListViewModel @Inject constructor(
    private val databaseDao: DaoShows
) : ViewModel() {


    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> get() = _appState

    private val _fetchUIState = MutableStateFlow<ShowsListUiState>(ShowsListUiState.Loading)
    val fetchUIState: StateFlow<ShowsListUiState> get() = _fetchUIState

    private val _databaseUIState = MutableStateFlow<DatabaseUiState>(DatabaseUiState.
    Loading)
    val databaseUIState: StateFlow<DatabaseUiState> get() = _databaseUIState

    init {
        onHomeSelected()
        fetchShows()
        getFavouritesShows()
    }

    fun onHomeSelected() {
        if(_appState.value.searchText.isNotBlank() && !_appState.value.isInDetails){
            fetchShows()
            _appState.value = _appState.value.copy(
                searchText = ""
            )
        }
        _appState.value = _appState.value.copy(
            title = "TV Shows",
            isHomeSelected = true,
            isFavoriteSelected = false,
            isInDetails = false
        )
    }

    fun onFavoriteSelected() {
        if(_appState.value.searchText.isNotBlank() && !_appState.value.isInDetails){
            fetchShows()
            _appState.value = _appState.value.copy(
                searchText = ""
            )
        }
        _appState.value = _appState.value.copy(
            title = "Favourites",
            isHomeSelected = false,
            isFavoriteSelected = true,
            isInDetails = false
        )
    }

    fun onShowDetails() {
        _appState.value = _appState.value.copy(
            title = "Show Details",
            isInDetails = true,
        )
    }

    fun onQueryChange(query: String) {
        _appState.value = _appState.value.copy(
            searchText = query
        )
    }

    fun onSearch() {
        fetchShowsPerName(_appState.value.searchText)
    }

    fun getFavouritesShows() {
        viewModelScope.launch {
            databaseDao.getAll()
                .onStart { _databaseUIState.value = DatabaseUiState.Loading }
                .catch { exception ->
                    _databaseUIState.value = DatabaseUiState.Error(exception.message ?: "Error desconocido")
                }
                .collect { shows ->
                    _databaseUIState.value = DatabaseUiState.Success(shows)
                }
        }
    }

    fun addShow(show: ShowDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseDao.insert(show)
        }
    }

    fun deleteShow(show: ShowDatabase) {
        viewModelScope.launch {
            databaseDao.delete(show)
        }
    }

    fun fetchShows() {
        viewModelScope.launch {
            ShowRepository.getShows().collect { uiState ->
                _fetchUIState.value = uiState
            }
        }
    }

    fun fetchShowsPerName(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                fetchShows()
            } else {
                ShowRepository.getShowsPerName(query).collect { uiState ->
                    _fetchUIState.value = uiState
                }
            }
        }
    }

    fun updateDatabase(show: Show, favouritesShows: List<ShowDatabase>){
        val showToAdd = show.toShowDatabase()

        if(showToAdd in favouritesShows){
            deleteShow(showToAdd)
        } else {
            addShow(showToAdd)
        }
    }

    fun searchFavourites(shows: List<Show>, favouritesShows: List<ShowDatabase>): List<Show> {
        val favouriteAsShows = favouritesShows.map { it.toShow() }

        val missingShows = favouriteAsShows.filter { favourite ->
            shows.none { it.id == favourite.id }
        }

        return  missingShows + shows
    }
}
