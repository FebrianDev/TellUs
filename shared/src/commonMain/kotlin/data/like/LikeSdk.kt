package data.like

import data.like.model.LikeRequest
import data.like.network.LikeApi
import data.like.state.LikeState

class LikeSdk {

    private val api = LikeApi()

//    @Throws(Exception::class)
//    suspend fun getLikeById(likeRequest: LikeRequest): LikeState {
//        return api.getLikeById(likeRequest)
//    }

    @Throws(Exception::class)
    suspend fun insertLike(likeRequest: LikeRequest): LikeState {
        return api.insertLike(likeRequest)
    }

}