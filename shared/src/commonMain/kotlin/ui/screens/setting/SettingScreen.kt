package ui.screens.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.CoroutineScope
import ui.components.AlertDialogComposable
import ui.components.TextBodyMedium
import ui.components.TopBar
import ui.screens.auth.RegisterScreen
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.KeyValueStorage
import utils.KeyValueStorageImpl

@Composable
fun SettingScreen() {

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()

    var openAlertDialog by remember { mutableStateOf(false) }
    var logoutState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().wrapContentSize()
    ) {
        TopBar("Setting")

        Spacer(modifier = Modifier.height(16.dp))

        ItemSetting(
            Icons.Filled.Person,
            "Profile"
        ) {

        }

        ItemSetting(
            Icons.Filled.Logout,
            "Logout"
        ) {
            openAlertDialog = true
        }

        if (openAlertDialog)
            AlertDialogComposable(
                {
                    openAlertDialog = false
                },
                {
                    openAlertDialog = false
                    //Logout Action
                    keyValueStorage.cleanStorage()
                    logoutState = true
                },
                "Logout",
                "Are you sure want to logout?"
            )

        if (logoutState)
            Navigator(
                RegisterScreen()
            )
    }
}

@Composable
fun ItemSetting(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Icon(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp),

                imageVector = icon,
                contentDescription = "Btn Bookmark",
                tint = colorPrimary
            )

            TextBodyMedium(text)
        }

        Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))
    }
}