import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import ui.screens.auth.RegisterScreen
import ui.screens.post.HomeScreen
import ui.themes.bgColor
import utils.KeyValueStorage
import utils.KeyValueStorageImpl

@Composable
fun App() {
    MaterialTheme {
        Surface(
            contentColor = bgColor,
            color = MaterialTheme.colors.primary
        ) {

            val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()

            if (keyValueStorage.observableIdUser.isEmpty()) {
                Navigator(
                    RegisterScreen()
                )
            } else {
                Navigator(
                    HomeScreen()
                )
            }

        }
    }
}

expect fun getPlatformName(): String