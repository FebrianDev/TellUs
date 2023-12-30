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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.chat.ChatEntity
import data.chat.ChatState
import data.chat.Message
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import ui.components.AlertDialogComposable
import ui.components.ProgressBarLoading
import ui.components.SpacerH
import ui.components.SpacerW
import ui.components.TextBodyBold
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.getDateNow
import utils.getUid
import utils.showSnackBar


class ChatRoomScreen(private val chatEntity: ChatEntity) : Screen {

    @Composable
    override fun Content() {

        val chatViewModel = getViewModel(Unit, viewModelFactory { ChatViewModel() })
        LaunchedEffect(false) {
            chatViewModel.getChat(chatEntity.id_chat)
            chatViewModel.readChat(chatEntity.id_chat, "")
        }

        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        val navigator = LocalNavigator.currentOrThrow

        var textName by remember { mutableStateOf("") }
        var textPostMessage by remember { mutableStateOf("") }
        var listMessages by remember { mutableStateOf(listOf<Message>()) }
        var showPostMessage by remember { mutableStateOf(false) }

        val uid = getUid()

        var uidState by remember { mutableStateOf("") }
        uidState = uid

        chatViewModel.getChat.collectAsState().value.onSuccess {
            when (it) {
                is ChatState.Loading -> {
                    ProgressBarLoading()
                }

                is ChatState.Error -> {
                    showSnackBar(it.message, coroutineScope, scaffoldState)
                }

                is ChatState.Success -> {
                    val chat = it.data
                    textName = chat.name
                    textPostMessage = chat.post_message

                    listMessages = chat.message

                }

                else -> {}
            }
        }.onFailure {
            showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
        }

        Column(modifier = Modifier.background(bgColor)) {
            Row(
                modifier = Modifier.fillMaxWidth().background(colorPrimary).padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        modifier = Modifier.size(24.dp).clickable {
                            navigator.pop()
                        },
                        contentDescription = "Back"
                    )

                    SpacerW(4.dp)

                    TextBodyBold(textName, modifier = Modifier.wrapContentSize(), bgColor)
                }

                Icon(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp).clickable {

                        },
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Options",
                    tint = bgColor
                )
            }

            Text(
                textPostMessage,
                color = colorPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).border(
                    BorderStroke(1.dp, colorPrimary), RoundedCornerShape(12.dp)
                ).fillMaxWidth().padding(8.dp).clickable {
                            showPostMessage = true
                }
            )

            SpacerH(8.dp)

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.9f).fillMaxWidth(),
            ) {
                if (uidState.isEmpty()) return@LazyColumn

                println("ListMessage" + listMessages.toList())
                items(listMessages) {
                    if (it.sender == uidState)
                        ItemSendChat(it)
                    else ItemReceiveChat(it)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            val message = Message(
                sender = uidState,
                prev_reply = "",
                img = "",
                read = false,
                date =  getDateNow()
            )

            SendChat(chatViewModel, message)

//            if(showPostMessage){
//                Dialog(
//                    onDismissRequest = { showPostMessage = false },
//                ) {
//                    Text(
//                        textPostMessage,
//                        color = colorPrimary,
//                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).border(
//                            BorderStroke(1.dp, colorPrimary), RoundedCornerShape(12.dp)
//                        ).fillMaxWidth().padding(8.dp).clickable {
//                            showPostMessage = true
//                        }
//                    )
//                }
//            }
        }
    }

    @Composable
    fun SendChat(
        chatViewModel: ChatViewModel,
        message: Message
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
                        message.message = textChat.text
                        chatViewModel.sendChat(chatEntity, message, LocalDate.toString())
                        chatViewModel.getChat(chatEntity.id_chat)
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