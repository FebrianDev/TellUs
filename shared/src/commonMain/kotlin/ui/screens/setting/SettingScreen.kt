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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mmk.kmpnotifier.notification.NotifierManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import openEmail
import openUrl
import ui.components.AlertDialogComposable
import ui.components.SpacerH
import ui.components.SpacerW
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

    var isExpandProfile by remember { mutableStateOf(false) }

    var openAboutState by remember { mutableStateOf(false) }

    var openAlertDialog by remember { mutableStateOf(false) }

    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier.fillMaxWidth().wrapContentSize()
    ) {
        TopBar("Setting")

        SpacerH(16.dp)

        ItemSetting(
            Icons.Filled.Person,
            "Profile"
        ) {
            isExpandProfile = !isExpandProfile
        }

        if (isExpandProfile) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                androidx.compose.material3.Icon(
                    Icons.Filled.Email,
                    contentDescription = "Email",
                    tint = colorPrimary
                )
                Text(
                    text = keyValueStorage.email, modifier = Modifier
                        .padding(start = 12.dp), fontSize = 16.sp,
                    color = colorPrimary
                )
            }
        }

        Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))

        ItemSetting(
            Icons.Filled.Info,
            "About"
        ) {
            openAboutState = true
        }

        if (openAboutState) {
            Dialog(onDismissRequest = { openAboutState = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = bgColor
                    ),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            text = "Tell Us",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tell Us is a social media where users to share and tell their stories to fellow users anonymously",
                            color = colorPrimary
                        )

                    }
                }
            }
        }
        Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))

        ItemSetting(
            Icons.Filled.Email,
            "Contact Me"
        ) {
            openEmail()
        }

        Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))

        ItemSetting(
            Icons.Filled.Link,
            "Developer Profile"
        ) {
            openUrl()
        }

        Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))

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
                    coroutineScope.launch {
                        NotifierManager.getPushNotifier().deleteMyToken()
                    }
                    navigator.push(RegisterScreen())
                },
                "Logout",
                "Are you sure want to logout?"
            )

        Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "TellUs 1.0.0",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 64.dp).fillMaxWidth()
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable(onClick = onClick)
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
            SpacerW(8.dp)
            TextBodyMedium(text)
        }
    }
}