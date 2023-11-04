package data.comment.state

import data.comment.model.CommentResponse
import data.response.ApiResponse

sealed class CommentState {
    data class Success(val data: ApiResponse<List<CommentResponse>>) : CommentState()
    data class Error(val message: String) : CommentState()
    object Loading : CommentState()
    object Empty : CommentState()

}