package data.chat

import data.response.ApiResponse

sealed class ChatState {
    data class Success(val data: ChatEntity) : ChatState()
    data class Error(val message: String) : ChatState()
    object Loading : ChatState()
    object Empty : ChatState()
}