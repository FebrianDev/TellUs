package data.post.state

import data.post.model.PostResponse
import data.response.ApiResponse

sealed class InsertPostState {
    data class Success(val data: ApiResponse<PostResponse>) : PostState()
    data class Error(val message: String) : PostState()
    object Loading : PostState()
    object Empty : PostState()
}