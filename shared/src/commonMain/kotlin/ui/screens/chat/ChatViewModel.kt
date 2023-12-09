package ui.screens.chat

import data.chat.ChatApi
import data.chat.ChatEntity
import data.chat.ChatState
import data.chat.ListChatState
import data.chat.Message
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val api = ChatApi()

    private var _createRoom = MutableStateFlow<Result<String>>(Result.success(""))
    val createRoom: StateFlow<Result<String>> get() = _createRoom

    private var _getListRoomChat =
        MutableStateFlow<Result<ListChatState>>(Result.success(ListChatState.Empty))
    val getListRoomChat: StateFlow<Result<ListChatState>> get() = _getListRoomChat

    private var _getChat =
        MutableStateFlow<Result<ChatState>>(Result.success(ChatState.Empty))
    val getChat: StateFlow<Result<ChatState>> get() = _getChat

    fun createRoom(
        chatEntity: ChatEntity
    ) {
        viewModelScope.launch {
            _createRoom.value = api.createRoom(chatEntity)
        }
    }

    fun getListRoomChat() {
        _getListRoomChat.value = Result.success(ListChatState.Loading)
        viewModelScope.launch {
            _getListRoomChat.value = Result.success(ListChatState.Success(api.getListRoomChat()))
        }
    }

    fun getChat(idChat: String) {
        _getChat.value = Result.success(ChatState.Loading)
        viewModelScope.launch {
            _getChat.value = api.getChat(idChat)
        }
    }

    fun sendChat(chatEntity: ChatEntity, message: Message, date: String) {
        viewModelScope.launch {
            // val chat = api.getChat("rZTjmoVjZFX7DUW3LuMA")

            api.sendChat(
                chatEntity,
//                Message(
//                    message = "Test123"
//                ), "rZTjmoVjZFX7DUW3LuMA",
                message, ""
            )
        }
    }
}