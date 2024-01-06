package ui.screens.chat

import data.chat.ChatApi
import data.chat.ChatEntity
import data.chat.ChatState
import data.chat.ListChatState
import data.chat.Message
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val api = ChatApi()

    private var _createRoom = MutableStateFlow<Result<String>>(Result.success(""))
    val createRoom: StateFlow<Result<String>> get() = _createRoom

    private var _getListRoomChat =
        MutableStateFlow<Result<ListChatState>>(Result.success(ListChatState.Empty))
    val getListRoomChat: StateFlow<Result<ListChatState>> get() = _getListRoomChat.asStateFlow()

    private var _getChat =
        MutableStateFlow<ChatState>(ChatState.Empty)
    val getChat: StateFlow<ChatState> get() = _getChat.asStateFlow()

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

    fun getChat(idChat: String) {
       // _getChat.value = Result.success(ChatState.Loading)
        CoroutineScope(Dispatchers.IO).launch  {
            _getChat.value = api.getChat(idChat)
        }
    }

//    fun getChat(idChat: String) {
//        _getChat.value = Result.success(ChatState.Loading)
//        CoroutineScope(Dispatchers.IO).launch  {
//            _getChat.value = api.getChat(idChat)
//        }
//    }

    fun sendChat(chatEntity: ChatEntity, message: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            api.sendChat(
                chatEntity,
                message
            )
        }
    }

    fun readChat(idChat: String, idSent: String) {
        CoroutineScope(Dispatchers.IO).launch {
            api.readChat(idChat, "")
        }
    }
}