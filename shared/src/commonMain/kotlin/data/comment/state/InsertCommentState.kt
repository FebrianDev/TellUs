package data.comment.state

import data.comment.model.CommentResponse
import data.response.ApiResponse

sealed class InsertCommentState {
    data class Success(val data: ApiResponse<CommentResponse>) : InsertCommentState()
    data class Error(val message: String) : InsertCommentState()
    object Loading : InsertCommentState()
    object Empty : InsertCommentState()

}