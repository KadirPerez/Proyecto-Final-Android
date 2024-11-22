package uabc.automatizacion.proyectofinal_shows.ui.model

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import uabc.automatizacion.proyectofinal_shows.data.model.Show
import uabc.automatizacion.proyectofinal_shows.navigation.ShowDetails

class ShowDetailModel(
    safeStateHandle: SavedStateHandle,
): ViewModel() {
    val showId: Int = safeStateHandle.toRoute<ShowDetails>().showId
    var shows: Show? = null

    fun init(context: Context){

    }
}