package uabc.automatizacion.proyectofinal_shows.ui.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TopBarViewModel : ViewModel() {
    val isSearching = mutableStateOf(false)
    val canBack = mutableStateOf(false)
    val title = mutableStateOf("")
}