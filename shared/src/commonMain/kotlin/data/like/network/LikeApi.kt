package data.like.network

import data.like.model.LikeRequest
import data.like.state.LikeState
import data.response.ApiErrorResponse
import data.utils.Constant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class LikeApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

//    suspend fun getLikeById(likeRequest: LikeRequest): LikeState {
//        val data = client.get("${Constant.BASE_URL}/api/like/${likeRequest.id_post}/${likeRequest.id_user}") {
//            contentType(ContentType.Application.Json)
//            headers {
//                append(
//                    "api-token",
//                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZlYnJpYW4yNjAyMjAwMUBnbWFpbC5jb20iLCJpYXQiOjE2OTg0MTIyMTksImV4cCI6MTcyOTk0ODIxOX0.ml0Vq86onfWUJnMUKdxeQCMIiP_uIpv7JbHXThp3r_U"
//                )
//            }
//        }
//        var likeState: LikeState = LikeState.Empty
//        when (data.status.value) {
//            200, 201 -> {
//                likeState = LikeState.Success(data.body())
//            }
//
//            in 400..404 -> {
//                likeState = LikeState.Error((data.body() as ApiErrorResponse).message)
//            }
//        }
//
//        return likeState
//    }

    suspend fun insertLike(likeRequest: LikeRequest): LikeState {
        val data = client.post("${Constant.BASE_URL}/api/like") {
            contentType(ContentType.Application.Json)
            setBody(likeRequest)
            headers {
                append(
                    "api-token",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZlYnJpYW4yNjAyMjAwMUBnbWFpbC5jb20iLCJpYXQiOjE2OTg0MTIyMTksImV4cCI6MTcyOTk0ODIxOX0.ml0Vq86onfWUJnMUKdxeQCMIiP_uIpv7JbHXThp3r_U"
                )
            }
        }
        var likeState: LikeState = LikeState.Empty
        when (data.status.value) {
            200, 201 -> {
                likeState = LikeState.Success(data.body())
            }

            in 400..404 -> {
                likeState = LikeState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return likeState
    }
}