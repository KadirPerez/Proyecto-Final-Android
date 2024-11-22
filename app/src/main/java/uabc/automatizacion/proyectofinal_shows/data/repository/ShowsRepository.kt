package uabc.automatizacion.proyectofinal_shows.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import uabc.automatizacion.proyectofinal_shows.data.remote.TVMazeApi
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
            response.body()?.let { shows ->
                emit(ShowsListUiState.Success(shows))
            } ?: emit(ShowsListUiState.Error("No shows found"))
        } else {
            emit(ShowsListUiState.Error("Failed to fetch shows: ${response.code()}"))
        }
    }.catch { e ->
        emit(ShowsListUiState.Error(e.localizedMessage ?: "An unknown error occurred"))
    }
}
