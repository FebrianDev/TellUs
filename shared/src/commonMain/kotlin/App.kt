import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import ui.screens.auth.RegisterScreen
import ui.screens.post.HomeScreen

@Composable
fun App() {
    MaterialTheme {

        Navigator(
           RegisterScreen()
        )
    }
}

expect fun getPlatformName(): String