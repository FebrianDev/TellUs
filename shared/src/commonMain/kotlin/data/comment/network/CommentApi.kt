package data.comment.network

import data.comment.model.CommentReplyRequest
import data.comment.model.CommentRequest
import data.comment.state.CommentState
import data.comment.state.InsertCommentState
import data.comment.state.ReplyCommentState
import data.response.ApiErrorResponse
import data.utils.Constant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CommentApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }

        install(HttpTimeout)
    }

    suspend fun insertComment(commentRequest: CommentRequest): InsertCommentState {
        val data = client.post("${Constant.BASE_URL}/api/comment") {
            contentType(ContentType.Application.Json)
            setBody(commentRequest)
            headers {
                append(
                    "api-token",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZlYnJpYW4yNjAyMjAwMUBnbWFpbC5jb20iLCJpYXQiOjE2OTg0MTIyMTksImV4cCI6MTcyOTk0ODIxOX0.ml0Vq86onfWUJnMUKdxeQCMIiP_uIpv7JbHXThp3r_U"
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }
        var commentState: InsertCommentState = InsertCommentState.Empty
        when (data.status.value) {
            200, 201 -> {
                commentState = InsertCommentState.Success(data.body())
            }

            in 400..404 -> {
                commentState = InsertCommentState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return commentState
    }

    suspend fun insertReplyComment(commentRequest: CommentReplyRequest): InsertCommentState {
        val data = client.post("${Constant.BASE_URL}/api/comment/reply") {
            contentType(ContentType.Application.Json)
            setBody(commentRequest)
            headers {
                append(
                    "api-token",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZlYnJpYW4yNjAyMjAwMUBnbWFpbC5jb20iLCJpYXQiOjE2OTg0MTIyMTksImV4cCI6MTcyOTk0ODIxOX0.ml0Vq86onfWUJnMUKdxeQCMIiP_uIpv7JbHXThp3r_U"
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }
        var commentState: InsertCommentState = InsertCommentState.Empty
        when (data.status.value) {
            200, 201 -> {
                commentState = InsertCommentState.Success(data.body())
            }

            in 400..404 -> {
                commentState = InsertCommentState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return commentState
    }

    suspend fun getCommentById(idPost: String): CommentState {
        val data = client.get("${Constant.BASE_URL}/api/comment/$idPost") {
            contentType(ContentType.Application.Json)
            headers {
                append(
                    "api-token",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZlYnJpYW4yNjAyMjAwMUBnbWFpbC5jb20iLCJpYXQiOjE2OTg0MTIyMTksImV4cCI6MTcyOTk0ODIxOX0.ml0Vq86onfWUJnMUKdxeQCMIiP_uIpv7JbHXThp3r_U"
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }

        var commentState: CommentState = CommentState.Empty
        when (data.status.value) {
            200, 201 -> {
                commentState = CommentState.Success(data.body())
            }

            in 400..404 -> {
                commentState = CommentState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return commentState
    }

    suspend fun getReplyComment(idPost: String, idComment: String): ReplyCommentState {
        val data = client.get("${Constant.BASE_URL}/api/comment/reply/$idPost/$idComment") {
            contentType(ContentType.Application.Json)
            headers {
                append(
                    "api-token",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZlYnJpYW4yNjAyMjAwMUBnbWFpbC5jb20iLCJpYXQiOjE2OTg0MTIyMTksImV4cCI6MTcyOTk0ODIxOX0.ml0Vq86onfWUJnMUKdxeQCMIiP_uIpv7JbHXThp3r_U"
                )
            }
            timeout {
                requestTimeoutMillis = 4000
            }
        }

        var replyCommentState: ReplyCommentState = ReplyCommentState.Empty
        when (data.status.value) {
            200, 201 -> {
                replyCommentState = ReplyCommentState.Success(data.body())
            }

            in 400..404 -> {
                replyCommentState =
                    ReplyCommentState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return replyCommentState
    }

}