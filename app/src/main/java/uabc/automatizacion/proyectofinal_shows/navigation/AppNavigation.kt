package uabc.automatizacion.proyectofinal_shows.navigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uabc.automatizacion.proyectofinal_shows.ui.views.ShowDetailsScreen
import uabc.automatizacion.proyectofinal_shows.ui.model.ShowDetailModel
import uabc.automatizacion.proyectofinal_shows.ui.model.ShowsListViewModel
import uabc.automatizacion.proyectofinal_shows.ui.model.TopBarViewModel
import uabc.automatizacion.proyectofinal_shows.ui.views.ShowsListScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    topBarModel: TopBarViewModel
){
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = ShowsList
    ){

        composable<ShowsList> {
            val vm : ShowsListViewModel = viewModel()

            topBarModel.canBack.value = false
            topBarModel.title.value = "Flick"
            topBarModel.isSearching.value = true

            vm.fetchShows()

            ShowsListScreen(vm)
        }

        composable<ShowDetails> {
            val vm : ShowDetailModel = viewModel()

            topBarModel.canBack.value = true
            topBarModel.title.value = "Flick"
            topBarModel.isSearching.value = false

            ShowDetailsScreen()
        }
    }
}