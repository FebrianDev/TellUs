package data.post.state

import data.post.model.PostResponse
import data.response.ApiResponse

sealed class ListPostState {
    data class Success(val data: ApiResponse<List<PostResponse>>) : ListPostState()
    data class Error(val message: String) : ListPostState()
    object Loading : ListPostState()
    object Empty : ListPostState()
}