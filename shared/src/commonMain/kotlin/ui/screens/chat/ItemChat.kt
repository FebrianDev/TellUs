package ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.chat.ChatEntity
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.components.SpacerH

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ItemChat(
    chatEntity: ChatEntity,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(top = 8.dp).fillMaxWidth().padding(horizontal = 16.dp)
            .clickable {
                onClick.invoke()
            }

    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource("drawable/icon_app.png"),
                contentDescription = ""
            )

            Spacer(modifier = Modifier.width(4.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    chatEntity.name,
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Text(
                    if (chatEntity.message.isEmpty()) ""
                    else chatEntity.message[chatEntity.message.size - 1].message,
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    minLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        SpacerH(4.dp)

        Divider()
    }

}