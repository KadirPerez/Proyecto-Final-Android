package uabc.automatizacion.proyectofinal_shows.ui.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uabc.automatizacion.proyectofinal_shows.data.model.Show

class SearchViewModel : ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToggleSearch(isActive: Boolean) {
        _isSearching.value = isActive
        if (!isActive) {
            onSearchTextChange("")
        }
    }
}
