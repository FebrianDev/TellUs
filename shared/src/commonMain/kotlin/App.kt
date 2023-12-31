import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import ui.screens.NotificationViewModel
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