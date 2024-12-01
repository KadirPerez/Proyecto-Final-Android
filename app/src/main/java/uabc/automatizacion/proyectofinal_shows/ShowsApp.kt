package uabc.automatizacion.proyectofinal_shows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import uabc.automatizacion.proyectofinal_shows.navigation.AppNavigation
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import uabc.automatizacion.proyectofinal_shows.data.database.AppDatabase
import uabc.automatizacion.proyectofinal_shows.navigation.Favourites
import uabc.automatizacion.proyectofinal_shows.navigation.ShowsList
import uabc.automatizacion.proyectofinal_shows.ui.model.ShowsListViewModel

@Composable
fun ShowsApp(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()

    val daoShows = AppDatabase.getInstance(LocalContext.current).showDao()
    val vm = ShowsListViewModel(daoShows)
    val appState by vm.appState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = appState.title,
                onSearch = vm::onSearch,
                searchQuery = appState.searchText,
                onQueryChange = vm::onQueryChange,
                isHomeSelected = appState.isHomeSelected,
                isInDetails = appState.isInDetails
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                ,
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        vm.onHomeSelected()
                        navController.navigate(ShowsList)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = "Inicio",
                        tint = if (appState.isHomeSelected) MaterialTheme.colorScheme.surfaceTint else Color.Black
                    )
                }

                IconButton(
                    onClick = {
                        vm.onFavoriteSelected()
                        navController.navigate(Favourites)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favoritos",
                        tint = if (appState.isFavoriteSelected) Color.Red else Color.Black
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navHostController = navController,
            modifier = Modifier.padding(innerPadding),
            showListViewModel = vm
        )
    }
}

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    isHomeSelected: Boolean,
    isInDetails: Boolean,
    onSearch: () -> Unit,
    onQueryChange: (String) -> Unit,
    searchQuery: String
){
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 35.dp)
            .padding(bottom = 10.dp)
            .padding(horizontal = 10.dp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start

    ){
        Text(
            modifier = modifier
                .padding(start = 5.dp)
                .padding(bottom = 10.dp),
            text = title,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color.Black
            )
        )

        if(isHomeSelected && !isInDetails){
            SearchBar(
                query = searchQuery,
                onQueryChange = onQueryChange,
                placeholder = "Search",
                onSearchClick =  onSearch
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    onSearchClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(text = placeholder)
        },
        modifier = Modifier
            .background(
                color =  MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(30.dp)
            )
            .fillMaxWidth(),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = {
                onSearchClick()
                focusManager.clearFocus()
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onSearchClick()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        )
    )
}