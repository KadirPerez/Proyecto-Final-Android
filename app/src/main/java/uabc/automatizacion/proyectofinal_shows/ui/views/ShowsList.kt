package uabc.automatizacion.proyectofinal_shows.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import uabc.automatizacion.proyectofinal_shows.data.model.Show
import uabc.automatizacion.proyectofinal_shows.ui.model.ShowsListViewModel
import uabc.automatizacion.proyectofinal_shows.ui.states.ShowsListUiState
import coil3.compose.rememberAsyncImagePainter
import uabc.automatizacion.proyectofinal_shows.data.database.ShowDatabase
import uabc.automatizacion.proyectofinal_shows.ui.states.DatabaseUiState
import java.lang.Thread.sleep

@Composable
fun ShowsListScreen(
    viewModel: ShowsListViewModel,
    onShowClick: (Show, isFavourite: Boolean) -> Unit,
    onFavouriteClick: (Show, List<ShowDatabase>) -> Unit,
    justFavourites: Boolean
) {
    val fetchUiState by viewModel.fetchUIState.collectAsState()
    val databaseUiState by viewModel.databaseUIState.collectAsState()

    when (fetchUiState) {
        is ShowsListUiState.Loading -> {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(text = "Loading shows")
            }
        }
        is ShowsListUiState.Success -> {
            val shows = (fetchUiState as ShowsListUiState.Success).shows
            when(databaseUiState) {
                is DatabaseUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Text(text = "Loading database")
                    }
                }
                is DatabaseUiState.Success -> {
                    val favouritesShows = (databaseUiState as DatabaseUiState.Success).favouritesShows
                    var justFavouritesShows = shows.filter { it.id in favouritesShows.map { it.id } }
                    justFavouritesShows = viewModel.searchFavourites(justFavouritesShows, favouritesShows)

                    ShowList(
                        shows = if (justFavourites) justFavouritesShows else shows,
                        onShowClick = onShowClick,
                        favouritesShows = favouritesShows,
                        onFavouriteClick = onFavouriteClick,
                        viewModel = viewModel
                    )
                }
                is DatabaseUiState.Error ->
                    Text(text = (databaseUiState as DatabaseUiState.Error).message)
            }
        }
        is ShowsListUiState.Error -> {
            Text(text = (fetchUiState as ShowsListUiState.Error).message)
        }
    }
}

@Composable
fun ShowList(
    shows: List<Show>,
    favouritesShows: List<ShowDatabase>,
    onShowClick: (Show, isFavourite: Boolean) -> Unit,
    onFavouriteClick: (Show, List<ShowDatabase>) -> Unit,
    viewModel: ShowsListViewModel
) {
    val appState by viewModel.appState.collectAsState()

    if(shows.isEmpty()){
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            if(appState.isHomeSelected){
                Text(text = "No shows found")
            } else {
                Text(text = "Your favourites are empty")
                Text(text = "(Long press to add one)")
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(shows) { show ->
                ShowCard(
                    show = show,
                    onShowClick = onShowClick,
                    isFavourite = favouritesShows.any { it.id == show.id },
                    onFavouriteClick = onFavouriteClick,
                    favouritesShows = favouritesShows
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowCard(
    show: Show,
    onShowClick: (Show, isFavourite: Boolean) -> Unit,
    onFavouriteClick: (Show, List<ShowDatabase>) -> Unit,
    modifier: Modifier = Modifier,
    isFavourite: Boolean = false,
    favouritesShows: List<ShowDatabase>
) {
    Card(
        modifier = modifier
            .height(170.dp)
            .combinedClickable(
                onClick = {
                    onShowClick(show, isFavourite)
                },
                onLongClick = {
                    onFavouriteClick(show, favouritesShows)
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor =
            if (isFavourite)
                MaterialTheme.colorScheme.surfaceTint
            else
                MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row (
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            ImageWithSpinner(imageUrl = show.image?.original ?: "")

            Column (
                modifier = modifier
                    .padding(horizontal = 10.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = show.name ?: "",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState())
                    )

                    if (isFavourite) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Star",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(25.dp)
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }

                Text(
                    text = show.genres?.joinToString() ?: "",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                )


                Row(
                    modifier = Modifier
                        .padding(top = 30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Start Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(17.dp)
                    )


                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .weight(1f)
                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                    ) {
                        val progressFraction = show.rating?.average?.toFloat()?.div(10.toFloat())?:0F

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction = progressFraction)
                                .background(Color.Blue, shape = RoundedCornerShape(8.dp))
                        )
                    }

                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "End Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(17.dp)
                    )
                }

                Text(
                    text = show.rating?.average.toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


@Composable
fun ImageWithSpinner(imageUrl: String) {
    val painter = rememberAsyncImagePainter(imageUrl)
    val painterState = painter.state.collectAsState()

    Box {
        Image(
            modifier = Modifier
                .width(100.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(10.dp)),
            painter = painter,
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )

        if (painterState.value is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center))
        }
    }
}