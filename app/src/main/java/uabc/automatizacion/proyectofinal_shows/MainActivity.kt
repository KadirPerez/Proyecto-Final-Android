package uabc.automatizacion.proyectofinal_shows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uabc.automatizacion.proyectofinal_shows.ui.theme.ProyectoFinalShowsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFinalShowsTheme {
                ShowsApp()
            }
        }
    }
}
