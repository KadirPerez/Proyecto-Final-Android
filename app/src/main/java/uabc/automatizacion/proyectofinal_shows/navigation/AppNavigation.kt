package uabc.automatizacion.proyectofinal_shows.navigation
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uabc.automatizacion.proyectofinal_shows.data.database.ShowDatabase
import uabc.automatizacion.proyectofinal_shows.ui.model.ShowDetailViewModel
import uabc.automatizacion.proyectofinal_shows.ui.model.ShowsListViewModel
import uabc.automatizacion.proyectofinal_shows.ui.views.ShowDetailsScreen
import uabc.automatizacion.proyectofinal_shows.ui.views.ShowsListScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    showListViewModel: ShowsListViewModel
){
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = ShowsList,
    ){

        composable<ShowsList> {
            BackHandler {}
            ShowsListScreen(
                viewModel = showListViewModel,
                onShowClick = { show, isFavourite ->
                    navHostController.navigate(ShowDetails(show.id?: 0, isFavourite))
                    showListViewModel.onShowDetails()
                },
                justFavourites = false,
                onFavouriteClick = { show , favouritesShows ->
                    showListViewModel.updateDatabase(show, favouritesShows)
                }
            )
        }

        composable<Favourites> {
            BackHandler {}
            ShowsListScreen(
                viewModel = showListViewModel,
                onShowClick = { show, isFavourite ->
                    navHostController.navigate(ShowDetails(show.id?: 0, isFavourite))
                    showListViewModel.onShowDetails()
                },
                justFavourites = true,
                onFavouriteClick = { show , favouritesShows ->
                    showListViewModel.updateDatabase(show, favouritesShows)
                }
            )
        }

        composable<ShowDetails>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            BackHandler {}
            ShowDetailsScreen()
        }
    }
}