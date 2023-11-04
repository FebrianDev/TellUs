package data.bookmark.state

import data.bookmark.model.BookmarkResponse
import data.response.ApiResponse

sealed class BookmarkState {
    data class Success(val data: ApiResponse<BookmarkResponse>) : BookmarkState()
    data class Error(val message: String) : BookmarkState()
    object Loading : BookmarkState()
    object Empty : BookmarkState()

}