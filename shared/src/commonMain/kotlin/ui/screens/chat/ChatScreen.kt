package ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.chat.ListChatState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import ui.components.EmptyState
import ui.components.ProgressBarLoading
import ui.components.TopBar
import ui.themes.bgColor
import utils.getUid
import utils.showSnackBar

@Composable
fun ChatScreen() {

    val navigator = LocalNavigator.currentOrThrow

    val scaffoldState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val chatViewModel = getViewModel(Unit, viewModelFactory { ChatViewModel() })
    chatViewModel.getListRoomChat()

    val uid = getUid()

    var uidState by remember { mutableStateOf("") }
    uidState = uid

    Scaffold(
        containerColor = bgColor
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentSize()
        ) {
            TopBar("Chat")
            Spacer(modifier = Modifier.height(16.dp))

            chatViewModel.getListRoomChat.collectAsState().value.onSuccess {

                when (it) {
                    is ListChatState.Loading -> {
                        ProgressBarLoading()
                    }

                    is ListChatState.Error -> {
                        showSnackBar(it.message, coroutineScope, scaffoldState)
                    }

                    is ListChatState.Success -> {
                        if (uidState.isEmpty()) return@onSuccess

                        val listChat =
                            it.data.filter { it.id_sent == uidState || it.id_receiver == uidState }
                                .toMutableStateList()

                        if (listChat.isEmpty()) {
                            EmptyState("drawable/ic_no_comment.png", "No Chat")
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxHeight(0.9f)) {
                                items(listChat ?: listOf()) { item ->
                                    ItemChat(item) {
                                        navigator.push(ChatRoomScreen(item.id_chat))
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }.onFailure {
                showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
            }

        }

    }
}

