package uabc.automatizacion.proyectofinal_shows

import uabc.automatizacion.proyectofinal_shows.navigation.AppNavigation
import uabc.automatizacion.proyectofinal_shows.ui.model.TopBarViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun ShowsApp(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()
    val vm : TopBarViewModel = viewModel()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = vm.title.value,
                canBack = vm.canBack.value,
                isSearching = vm.isSearching.value,
                onBack = {
                    if (vm.canBack.value){
                        navController.popBackStack()
                    }
                }
            )
        },
    ) { innerPadding ->
        AppNavigation(
            navHostController = navController,
            modifier = Modifier.padding(innerPadding),
            topBarModel = vm
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    canBack: Boolean,
    isSearching: Boolean,
    onBack: () -> Unit
){
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        navigationIcon = {
            if (canBack){
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if(isSearching) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        }
    )
}