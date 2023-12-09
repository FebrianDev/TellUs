package data.chat

import data.response.ApiResponse

sealed class ListChatState {
    data class Success(val data: List<ChatEntity>) : ListChatState()
    data class Error(val message: String) : ListChatState()
    object Loading : ListChatState()
    object Empty : ListChatState()
}