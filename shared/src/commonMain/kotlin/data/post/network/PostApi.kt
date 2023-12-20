package data.post.network

import data.post.model.PostRequest
import data.post.model.PrivatePostRequest
import data.post.state.ListPostState
import data.post.state.PostState
import data.response.ApiErrorResponse
import data.utils.Constant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.timeout
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class PostApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
        install(HttpTimeout)
    }

    suspend fun getAllPost(apiToken: String): ListPostState {
        val data = client.get("${Constant.BASE_URL}/api/post") {
            contentType(ContentType.Application.Json)
            headers {
                append(
                    "api-token",
                    apiToken
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }

        var listPostState: ListPostState = ListPostState.Empty
        when (data.status.value) {
            200, 201 -> {
                listPostState = ListPostState.Success(data.body())
            }

            in 400..404 -> {
                listPostState = ListPostState.Error((data.body() as ApiErrorResponse).message)
            }

        }

        return listPostState
    }

    suspend fun getTrendingPost(apiToken: String): ListPostState {
        val data = client.get("${Constant.BASE_URL}/api/post/all/trending") {
            contentType(ContentType.Application.Json)
            headers {
                append(
                    "api-token",
                    apiToken
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }

        var listPostState: ListPostState = ListPostState.Empty
        when (data.status.value) {
            200, 201 -> {
                listPostState = ListPostState.Success(data.body())
            }

            in 400..404 -> {
                listPostState = ListPostState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return listPostState
    }

    suspend fun getPostByIdUser(id: String, apiToken: String): ListPostState {
        val data = client.get("${Constant.BASE_URL}/api/post/user/$id") {
            contentType(ContentType.Application.Json)
            headers {
                append(
                    "api-token",
                    apiToken
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }

        var listPostState: ListPostState = ListPostState.Empty
        when (data.status.value) {
            200, 201 -> {
                listPostState = ListPostState.Success(data.body())
            }

            in 400..404 -> {
                listPostState = ListPostState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return listPostState
    }

    suspend fun getPostByTag(tag: String, apiToken: String): ListPostState {
        val data = client.get("${Constant.BASE_URL}/api/post/tag/$tag") {
            contentType(ContentType.Application.Json)
            headers {
                append(
                    "api-token",
                    apiToken
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }

        var listPostState: ListPostState = ListPostState.Empty
        when (data.status.value) {
            200, 201 -> {
                listPostState = ListPostState.Success(data.body())
            }

            in 400..404 -> {
                listPostState = ListPostState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return listPostState
    }

    suspend fun getPostById(id: String, apiToken: String): PostState {
        val data = client.get("${Constant.BASE_URL}/api/post/$id") {
            contentType(ContentType.Application.Json)
            headers {
                append(
                    "api-token",
                    apiToken
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }

        var postState: PostState = PostState.Empty
        when (data.status.value) {
            200, 201 -> {
                postState = PostState.Success(data.body())
            }

            in 400..404 -> {
                postState = PostState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return postState
    }

    suspend fun deletePost(id: String, apiToken: String): PostState {
        val data = client.delete("${Constant.BASE_URL}/api/post/$id") {
            contentType(ContentType.Application.Json)
            headers {
                append(
                    "api-token",
                    apiToken
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }

        var postState: PostState = PostState.Empty
        when (data.status.value) {
            200, 201 -> {
                postState = PostState.Success(data.body())
            }

            in 400..404 -> {
                postState = PostState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return postState
    }

    suspend fun insertPost(postRequest: PostRequest, apiToken: String): PostState {
        val data = client.post("${Constant.BASE_URL}/api/post") {
            contentType(ContentType.Application.Json)
            setBody(postRequest)
            headers {
                append(
                    "api-token",
                    apiToken
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }
        var postState: PostState = PostState.Empty

        when (data.status.value) {
            200, 201 -> {
                postState = PostState.Success(data.body())
            }

            in 400..404 -> {
                postState = PostState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return postState
    }

    suspend fun insertLike(postRequest: PostRequest, apiToken: String): PostState {
        val data = client.post("${Constant.BASE_URL}/api/like") {
            contentType(ContentType.Application.Json)
            setBody(postRequest)
            headers {
                append(
                    "api-token",
                    apiToken
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }
        var postState: PostState = PostState.Empty
        when (data.status.value) {
            200, 201 -> {
                postState = PostState.Success(data.body())
            }

            in 400..404 -> {
                postState = PostState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return postState
    }

    suspend fun changePrivatePost(
        privatePostRequest: PrivatePostRequest,
        idPost: String,
        apiToken: String
    ): PostState {
        val data = client.put("${Constant.BASE_URL}/api/post/${idPost}") {
            contentType(ContentType.Application.Json)
            setBody(privatePostRequest)
            headers {
                append(
                    "api-token",
                    apiToken
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }
        var postState: PostState = PostState.Empty
        when (data.status.value) {
            200, 201 -> {
                postState = PostState.Success(data.body())
            }

            in 400..404 -> {
                postState = PostState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return postState
    }

}