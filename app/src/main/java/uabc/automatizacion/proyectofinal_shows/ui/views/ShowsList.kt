package uabc.automatizacion.proyectofinal_shows.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import uabc.automatizacion.proyectofinal_shows.data.model.Show
import uabc.automatizacion.proyectofinal_shows.ui.model.ShowsListViewModel
import uabc.automatizacion.proyectofinal_shows.ui.states.ShowsListUiState
import coil3.compose.rememberAsyncImagePainter

@Composable
fun ShowsListScreen(viewModel: ShowsListViewModel) {
    val uiState by viewModel.showListUIState.collectAsState()

    when (uiState) {
        is ShowsListUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ShowsListUiState.Success -> {
            val shows = (uiState as ShowsListUiState.Success).shows
            ShowList(shows)
        }
        is ShowsListUiState.Error -> {
            Text(text = (uiState as ShowsListUiState.Error).message)
        }
    }
}

@Composable
fun ShowList(
    shows: List<Show>
){
    LazyColumn{
        items(shows){ show ->
            ShowCard(show = show)
        }
    }
}

@Composable
fun ShowCard(
    show: Show
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .padding(vertical = 10.dp)
            .padding(horizontal = 10.dp)
            .clickable(onClick = {  }),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            ImageWithSpinner(show.image?.medium ?: "")
            Text(
                modifier = Modifier
                    .padding(horizontal = 15.dp),
                text = show.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier,
                text = show.rating.average.toString(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ImageWithSpinner(imageUrl: String) {
    val painter = rememberAsyncImagePainter(imageUrl)
    val painterState = painter.state.collectAsState()

    Box(
        modifier = Modifier.width(100.dp).fillMaxHeight()
    ) {
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        if (painterState.value is AsyncImagePainter.State.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}