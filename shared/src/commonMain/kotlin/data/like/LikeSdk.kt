package data.like

import data.like.model.LikeRequest
import data.like.network.LikeApi
import data.like.state.LikeState

class LikeSdk {

    private val api = LikeApi()

    @Throws(Exception::class)
    suspend fun insertLike(likeRequest: LikeRequest, apiToken: String): Result<LikeState> {
        return try {
            Result.success(api.insertLike(likeRequest, apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}