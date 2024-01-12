import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import data.chat.ChatEntity
import data.chat.Message
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

            println("OSSYSTEM" + getPlatformName())
        }
    }
}

expect fun getPlatformName(): String

expect fun sharePost()

expect fun openEmail()

expect fun openUrl()

expect fun getChat(idChat: String, onGetChat: (chat: ChatEntity) -> Unit)
expect fun readChat(
    idChat: String,
    idSent: String
)

expect fun sendChat(message: Message,
                    idSent: String,
                    idReceiver: String,
                    idChat: String)