import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
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

            val notifier = NotifierManager.getLocalNotifier()
            notifier.notify("Title", "Body")

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