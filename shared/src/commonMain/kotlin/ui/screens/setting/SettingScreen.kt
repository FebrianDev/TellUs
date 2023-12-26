package ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.components.AlertDialogComposable
import ui.components.SpacerW
import ui.components.TextBodyMedium
import ui.components.TopBar
import ui.screens.auth.RegisterScreen
import ui.themes.bgColor
import ui.themes.colorPrimary
import ui.themes.colorSecondary
import utils.KeyValueStorage
import utils.KeyValueStorageImpl

@Composable
fun SettingScreen() {

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()

    var isExpandProfile by remember { mutableStateOf(false) }

    var isActive by remember { mutableStateOf(false) }

    var openAboutState by remember { mutableStateOf(false) }

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
                    text = "febrian26022001@gmail.com", modifier = Modifier
                        .padding(start = 12.dp), fontSize = 16.sp,
                    color = colorPrimary
                )
            }
        }

        Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))

        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Icon(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp),

                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Btn Bookmark",
                        tint = colorPrimary
                    )
                    SpacerW(8.dp)
                    TextBodyMedium("Notification")
                }

                Switch(
                    checked = isActive,
                    onCheckedChange = {
                        isActive = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorPrimary,
                        checkedTrackColor = colorSecondary,
                        uncheckedThumbColor = colorPrimary,
                        uncheckedTrackColor = bgColor
                    )
                )
            }
            Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))
        }

        var isColorSystem by remember { mutableStateOf(false) }

        ItemSetting(
            Icons.Filled.Palette,
            "Color System"
        ) {
            isColorSystem = !isColorSystem
        }

        if (isColorSystem) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                        .border(
                            width = 3.dp,
                            color = //if (addEditViewModel.todoColor.value == colorInt) {
                            Color.Black,
                            // } else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable {
                            coroutineScope.launch {
//                                todoBackgroundAnimatable.animateTo(
//                                    targetValue = Color(colorInt),
//                                    animationSpec = tween(
//                                        durationMillis = 500
//                                    )
//                                )
                            }
                            //addEditViewModel.onEvent(AddEditTodoEvent.ChangeColor(colorInt))
                        }
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
                            text = "FDev Project",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "FDev Project is a team based in Indonesia that develops (Share - Tell Your Stories) for users can share their stories anonymously",
                            color = colorPrimary
                        )

                    }
                }
            }
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
                    logoutState = true
                },
                "Logout",
                "Are you sure want to logout?"
            )

        if (logoutState)
            Navigator(
                RegisterScreen()
            )
        Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))
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