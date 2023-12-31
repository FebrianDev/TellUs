import androidx.compose.runtime.Composable
import com.febriandev.common.R
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

actual fun getPlatformName(): String = "Android"

@Composable fun MainView() = App()
