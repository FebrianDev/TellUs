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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mmk.kmpnotifier.notification.NotifierManager
import data.bookmark.network.NotificationData
import data.bookmark.network.NotificationRequest
import data.chat.ChatEntity
import data.chat.Message
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import getChat
import getPlatformName
import kotlinx.coroutines.launch
import readChat
import sendChat
import ui.components.SpacerH
import ui.components.SpacerW
import ui.components.TextBodyBold
import ui.screens.NotificationViewModel
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.getDateNow
import utils.getIdUser
import utils.keyValueStorage


class ChatRoomScreen(private var chatEntity: ChatEntity) : Screen {

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {

        val chatViewModel = getViewModel(Unit, viewModelFactory { ChatViewModel() })
        val notificationViewModel = getViewModel(Unit, viewModelFactory { NotificationViewModel() })

        val uid = getIdUser()

        LaunchedEffect(true) {
            NotifierManager.getPushNotifier().subscribeToTopic("topic")
            readChat(chatEntity.id_chat, uid)

        }

        val coroutineScope = rememberCoroutineScope()

        val lazyListState = rememberLazyListState()

        val navigator = LocalNavigator.currentOrThrow

        var textName by remember { mutableStateOf("") }
        var textPostMessage by remember { mutableStateOf("") }
        val listMessages by remember { mutableStateOf(listOf<Message>().toMutableStateList()) }
        var showPostMessage by remember { mutableStateOf(false) }

        var chat by remember { mutableStateOf(chatEntity) }

        getChat(chatEntity.id_chat) {
            chat = it
            listMessages.clear()
            listMessages.addAll(chat.message)

            coroutineScope.launch {
                if (it.message.size > 0)
                    lazyListState.scrollToItem(index = it.message.size - 1)
            }
        }

        textName = chatEntity.name
        textPostMessage = chatEntity.post_message

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


                OptionChat() {
                    chatViewModel.deleteChat(chatEntity.id_chat)
                    navigator.pop()
                }
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
                state = lazyListState
            ) {
                if (uid.isEmpty()) return@LazyColumn

                items(listMessages) {
                    if (it.sender == uid)
                        ItemSendChat(it)
                    else ItemReceiveChat(it)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Send Chat
            val message = Message(
                sender = uid,
                prev_reply = "",
                img = "",
                read = false,
                date = getDateNow()
            )

            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current

            Row(
                modifier = Modifier.fillMaxWidth().background(bgColor),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                var textChat by remember { mutableStateOf("") }

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

                            if (textChat.isEmpty()) return@clickable

                            if (getPlatformName() == "Android") {
                                notificationViewModel.sendNotification(
                                    NotificationRequest(
                                        NotificationData(
                                            "Someone sent you a message",
                                            textChat
                                        ),
                                        if (chatEntity.token_sent == keyValueStorage.fcmToken) chatEntity.token_receiver else chatEntity.token_sent
                                    )
                                )
                            }

                            coroutineScope.launch {
                                lazyListState.animateScrollToItem(index = listMessages.size)
                            }

                            //Send Chat
                            message.message = textChat
                            sendChat(
                                message,
                                chatEntity.id_sent,
                                chatEntity.id_receiver,
                                chatEntity.id_chat
                            )
                            textChat = ""
                            keyboardController?.hide()
                            focusManager.clearFocus(true)
                        }
                )

                if (showPostMessage) {
                    AlertDialog(
                        containerColor = bgColor,
                        onDismissRequest = { showPostMessage = false },
                        confirmButton = {
                        },

                        title = {
                        },
                        text = {

                            Text(
                                textPostMessage,
                                color = colorPrimary,
                                modifier = Modifier.border(
                                    BorderStroke(1.dp, colorPrimary), RoundedCornerShape(12.dp)
                                ).fillMaxWidth().padding(8.dp).clickable {
                                    showPostMessage = true
                                }
                            )

                        },
                    )

                }
            }
        }

    }
}