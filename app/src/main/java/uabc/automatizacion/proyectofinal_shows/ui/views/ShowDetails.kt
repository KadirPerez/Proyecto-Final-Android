package uabc.automatizacion.proyectofinal_shows.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uabc.automatizacion.proyectofinal_shows.data.model.Show
import uabc.automatizacion.proyectofinal_shows.ui.model.ShowDetailViewModel
import uabc.automatizacion.proyectofinal_shows.ui.model.ShowsListViewModel
import uabc.automatizacion.proyectofinal_shows.ui.states.ShowDetailsUiState
import org.jsoup.Jsoup

@Composable
fun ShowDetailsScreen(){

    val viewModel : ShowDetailViewModel = viewModel()
    val uiState by viewModel.showDetailUIState.collectAsState()

    when (uiState) {
        is ShowDetailsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ShowDetailsUiState.Success -> {
            val show = (uiState as ShowDetailsUiState.Success).show

            ShowDetails(show = show, isFavourite = viewModel.isFavourite)
        }
        is ShowDetailsUiState.Error -> {
            Text(text = (uiState as ShowDetailsUiState.Error).message)
        }
    }
}

@Composable
fun ShowDetails(
    show: Show,
    isFavourite: Boolean
){
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row (
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            ImageWithSpinner(imageUrl = show.image?.original ?: "")

            Column (
                modifier = Modifier
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
                                .size(35.dp)
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }

                Text(
                    text = "Generes: " + show.genres?.joinToString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                )

                Text(
                    text = "Release date: " + show.premiered,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = "Lenguage: " + show.language,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )

                Text(
                    text = "Region: " + show.network?.country?.name,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 30.dp).padding(horizontal = 10.dp),
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

        Text(
            modifier = Modifier.padding(25.dp),
            text = Jsoup.parse(show.summary?: "").text(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Justify
        )
    }
}
