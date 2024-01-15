package ui.screens.chat

import data.chat.ChatApi
import data.chat.ChatEntity
import data.chat.ListChatState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val api = ChatApi()

    private var _createRoom = MutableStateFlow(Result.success(""))
    val createRoom: StateFlow<Result<String>> get() = _createRoom

    private var _getListRoomChat =
        MutableStateFlow<Result<ListChatState>>(Result.success(ListChatState.Empty))
    val getListRoomChat: StateFlow<Result<ListChatState>> get() = _getListRoomChat.asStateFlow()

    fun createRoom(
        chatEntity: ChatEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            _createRoom.value = api.createRoom(chatEntity)
        }
    }

    fun getListRoomChat() {
        _getListRoomChat.value = Result.success(ListChatState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _getListRoomChat.value = Result.success(ListChatState.Success(api.getListRoomChat()))
        }
    }


    fun deleteChat(idChat: String) {
        CoroutineScope(Dispatchers.IO).launch {
            api.deleteChat(idChat)
        }
    }
}