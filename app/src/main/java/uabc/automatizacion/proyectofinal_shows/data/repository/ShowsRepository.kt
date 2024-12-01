package uabc.automatizacion.proyectofinal_shows.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import uabc.automatizacion.proyectofinal_shows.data.model.Show
import uabc.automatizacion.proyectofinal_shows.data.remote.TVMazeApi
import uabc.automatizacion.proyectofinal_shows.ui.states.ShowDetailsUiState
import uabc.automatizacion.proyectofinal_shows.ui.states.ShowsListUiState

object ShowRepository {

    fun getShows(): Flow<ShowsListUiState> = flow {
        emit(ShowsListUiState.Loading)
        val response = TVMazeApi.service.getShows()
        if (response.isSuccessful) {
            response.body()?.let { shows ->
                emit(ShowsListUiState.Success(shows)) // Emit success with data
            } ?: emit(ShowsListUiState.Error("No shows found"))
        } else {
            emit(ShowsListUiState.Error("Failed to fetch shows: ${response.code()}"))
        }
    }.catch { e ->
        emit(ShowsListUiState.Error(e.localizedMessage ?: "An unknown error occurred"))
    }

    fun getShowsPerName(query: String): Flow<ShowsListUiState> = flow {
        emit(ShowsListUiState.Loading)
        val response = TVMazeApi.service.getShowsPerName(query)
        if (response.isSuccessful) {
            response.body()?.let { showResponses ->
                val shows = showResponses.map { it.show }
                emit(ShowsListUiState.Success(shows))
            } ?: emit(ShowsListUiState.Error("No shows found"))
        } else {
            emit(ShowsListUiState.Error("Failed to fetch shows: ${response.code()}"))
        }
    }.catch { e ->
        emit(ShowsListUiState.Error(e.localizedMessage ?: "An unknown error occurred"))
    }

    fun getShowPerId(id: Int): Flow<ShowDetailsUiState> = flow {
        emit(ShowDetailsUiState.Loading)
        val response = TVMazeApi.service.getShowById(id)

        if (response.isSuccessful) {
            response.body()?.let { show ->
                emit(ShowDetailsUiState.Success(show))
            } ?: emit(ShowDetailsUiState.Error("Show not found"))
        } else {
            emit(ShowDetailsUiState.Error("Failed to fetch show: ${response.code()}"))
        }
    }.catch { e ->
        emit(ShowDetailsUiState.Error(e.localizedMessage ?: "An unknown error occurred"))
    }

}
