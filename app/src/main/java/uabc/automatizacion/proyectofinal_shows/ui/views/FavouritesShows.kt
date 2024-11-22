package uabc.automatizacion.proyectofinal_shows.ui.views

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import uabc.automatizacion.proyectofinal_shows.data.model.Show

@Composable
fun FavouritesShowsScreen(
    favouritesShowsList: List<Show>
){
    Text(
        text = "Favourites Shows",
    )
}