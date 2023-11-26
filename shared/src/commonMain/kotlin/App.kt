import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ui.screens.auth.RegisterScreen
import ui.screens.post.HomeScreen
import ui.themes.bgColor
import utils.KeyValueStorage
import utils.KeyValueStorageImpl
import utils.getUid

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App() {
    MaterialTheme {
        Surface(
            contentColor = bgColor,
            color = MaterialTheme.colors.primary
        ) {

            val coroutineScope: CoroutineScope = rememberCoroutineScope()
            val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()

            var uidState by remember { mutableStateOf("") }

            coroutineScope.launch {
                keyValueStorage.observableUid?.collectLatest { uid ->
                    uidState = uid
                }
            }

            val uid = getUid()
            println("UidState" + uid)

            if (uidState.isNotEmpty()) {
                Navigator(
                    HomeScreen()
                )
            } else {
                Navigator(
                    RegisterScreen()
                )
            }

        }
    }
}

expect fun getPlatformName(): String