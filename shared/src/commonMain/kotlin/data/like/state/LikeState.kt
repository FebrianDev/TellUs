package data.like.state

import data.like.model.InsertLikeResponse
import data.response.ApiResponse

sealed class LikeState {
    data class Success(val data: ApiResponse<InsertLikeResponse>) : LikeState()
    data class Error(val message: String) : LikeState()
    object Loading : LikeState()
    object Empty : LikeState()
}