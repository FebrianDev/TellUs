package ui.screens.setting

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ui.components.TextBodyMedium
import ui.components.TopBar
import ui.themes.colorPrimary

@Composable
fun SettingScreen() {
    Column(
        modifier = Modifier.fillMaxWidth().wrapContentSize()
    ) {
        TopBar("Setting")

        Spacer(modifier = Modifier.height(16.dp))

        ItemSetting(
            Icons.Filled.Person,
            "Profile"
        )
    }
}

@Composable
fun ItemSetting(
    icon: ImageVector,
    text: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
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

        Divider()
    }
}