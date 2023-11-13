package data.comment.state

import data.comment.model.ReplyCommentResponse
import data.response.ApiResponse

sealed class ReplyCommentState {
    data class Success(val data: ApiResponse<List<ReplyCommentResponse>>) : ReplyCommentState()
    data class Error(val message: String) : ReplyCommentState()
    object Loading : ReplyCommentState()
    object Empty : ReplyCommentState()
}