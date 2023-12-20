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
                keyValueStorage.observableApiToken?.collectLatest { uid ->
                    uidState = uid
                }
            }

            val uid = getUid()
            println("UidState" + uid)

            val state = keyValueStorage.observableApiToken?.collectAsState("")?.value
            if (state?.isEmpty() == true) {
                Navigator(
                    RegisterScreen()
                )
            } else if(state?.isNotEmpty() == true) {
                Navigator(
                    HomeScreen()
                )
            }


            //    if (uidState.isNotEmpty()) {
//                Navigator(
//                    HomeScreen()
//                )
//            } else {
//                Navigator(
//                    RegisterScreen()
//                )
//            }

//            val date = Clock.System.now().toString()
//            val convert = Instant.parse(date)
//            val toLocalDate = convert.toLocalDateTime(TimeZone.currentSystemDefault())
//            val getTime = toLocalDate.time.toString().substring(0, 5)
//            val getDate = toLocalDate.date
//            println("Date2 " + date)
//            println("Date2 " + convert)
//            println("Date2 " + toLocalDate)
//            println("Date2 " + getTime)
//            println("Date2 " + getDate)

//            NotifierManager.addListener(object : NotifierManager.Listener {
//                override fun onNewToken(token: String) {
//                    println("onNewToken: $token") //Update user token in the server if needed
//                }
//            })

        }
    }
}

expect fun getPlatformName(): String