package data.bookmark.state

import data.bookmark.model.BookmarkResponse
import data.post.model.PostResponse
import data.response.ApiResponse

sealed class ListBookmarkState {
    data class Success(val data: ApiResponse<List<PostResponse>>) : ListBookmarkState()
    data class Error(val message: String) : ListBookmarkState()
    object Loading : ListBookmarkState()
    object Empty : ListBookmarkState()

}