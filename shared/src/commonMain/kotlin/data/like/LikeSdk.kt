package data.like

import data.like.model.LikeRequest
import data.like.network.LikeApi
import data.like.state.LikeState

class LikeSdk {

    private val api = LikeApi()

    @Throws(Exception::class)
    suspend fun insertLike(likeRequest: LikeRequest): Result<LikeState> {
        return try {
            Result.success(api.insertLike(likeRequest))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}