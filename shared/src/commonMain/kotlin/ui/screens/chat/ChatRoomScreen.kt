package ui.screens.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import ui.components.TextBodyBold
import ui.themes.bgColor
import ui.themes.colorPrimary


class ChatRoomScreen : Screen {

    @Composable
    override fun Content() {

        val chatViewModel = getViewModel(Unit, viewModelFactory { ChatViewModel() })

        Column(modifier = Modifier.background(bgColor)) {

            Row(
                modifier = Modifier.fillMaxWidth().background(colorPrimary).padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        modifier = Modifier.size(24.dp),
                        contentDescription = "Back"
                    )

                    Spacer(Modifier.width(4.dp))

                    TextBodyBold("Virgie", modifier = Modifier.wrapContentSize(), bgColor)
                }

                Icon(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Options",
                    tint = bgColor
                )
            }

            Text(
                "Hello World",
                color = colorPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).border(
                    BorderStroke(1.dp, colorPrimary), RoundedCornerShape(12.dp)
                ).fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            val a = 10

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.9f).fillMaxWidth(),
            ) {
                items(a) {
                    if (it % 2 == 0)
                        ItemReceiveChat()
                    else ItemSendChat()
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            SendChat(chatViewModel)
        }
    }

    @Composable
    fun SendChat(
        chatViewModel: ChatViewModel,
        //  scaffoldState: SnackbarHostState,
        //  coroutineScope: CoroutineScope,
        //  uid: String
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(bgColor),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            var textChat by remember { mutableStateOf(TextFieldValue("")) }

            TextField(
                value = textChat,
                onValueChange = {
                    textChat = it
                },
                placeholder = { androidx.compose.material3.Text(text = "Type your message") },
                modifier = Modifier.fillMaxWidth(0.8f).background(bgColor),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = bgColor,
                    unfocusedContainerColor = bgColor,
                    disabledContainerColor = bgColor,
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            TextBodyBold(
                text = "Send",
                modifier = Modifier.padding(start = 4.dp, end = 16.dp).wrapContentSize()
                    .align(Alignment.CenterVertically).clickable {

                        //Send Chat

                        textChat = TextFieldValue("")
                    }
            )
        }

//        commentViewModel.insertCommentState.collectAsState().value.onSuccess {
//            when (it) {
//                is InsertCommentState.Loading -> {
//                    ProgressBarLoading()
//                }
//
//                is InsertCommentState.Error -> {
//                    showSnackBar("Something was wrong!", coroutineScope, scaffoldState)
//                }
//
//                is InsertCommentState.Success -> {
//                    commentViewModel.getCommentById(id.toString())
//                }
//
//                else -> {
//
//                }
//            }
//        }.onFailure {
//            showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
//        }

    }
}