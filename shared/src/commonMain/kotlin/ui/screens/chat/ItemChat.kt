package ui.screens.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.chat.ChatEntity
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.components.SpacerH
import ui.components.SpacerW
import ui.themes.colorPrimary
import utils.getIdUser

@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
@Composable
fun ItemChat(
    chatEntity: ChatEntity,
    onClick: () -> Unit = {}
) {

    val uid = getIdUser()

    Column(
        modifier = Modifier.padding(top = 8.dp).fillMaxWidth().padding(horizontal = 16.dp)
            .combinedClickable(
                onLongClick = {},
                onClick = {
                    onClick.invoke()
                }
            )

    ) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            Row {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource("drawable/icon_app.png"),
                    contentDescription = ""
                )

                SpacerW(4.dp)

                Column{

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

                    SpacerH(4.dp)
                }
            }

            println("CountChat ${chatEntity.id_sent} ${chatEntity.id_receiver} $uid "+chatEntity.countReadSent +" "+chatEntity.countReadReceiver )

            if (chatEntity.id_sent == uid) {
                if (chatEntity.countReadSent > 0) {
                    //Read
                    Text(
                        text = chatEntity.countReadSent.toString(),
                        color = Color.White,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colorPrimary)
                            .size(32.dp)
                            .padding(4.dp),
                        fontSize = 16.sp, maxLines = 1, textAlign = TextAlign.Center
                    )
                }
            } else {
                if (chatEntity.countReadReceiver > 0) {
                    Text(
                        text = chatEntity.countReadReceiver.toString(),
                        color = Color.White,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colorPrimary)
                            .size(32.dp)
                            .padding(4.dp),
                        fontSize = 16.sp, maxLines = 1, textAlign = TextAlign.Center
                    )
                }
            }
        }

        SpacerH(4.dp)

        Divider()
    }

}