package data.bookmark.network

import data.bookmark.state.BookmarkState
import data.bookmark.model.BookmarkRequest
import data.bookmark.state.ListBookmarkState
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

class BookmarkApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })

            install(HttpTimeout)
        }
    }

    suspend fun insertBookmark(bookmarkRequest: BookmarkRequest): BookmarkState {
        val data = client.post("${Constant.BASE_URL}/api/bookmark") {
            contentType(ContentType.Application.Json)
            setBody(bookmarkRequest)
            headers {
                append(
                    "api-token",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZlYnJpYW4yNjAyMjAwMUBnbWFpbC5jb20iLCJpYXQiOjE2OTg0MTIyMTksImV4cCI6MTcyOTk0ODIxOX0.ml0Vq86onfWUJnMUKdxeQCMIiP_uIpv7JbHXThp3r_U"
                )
            }
        }

        var bookmarkState: BookmarkState = BookmarkState.Empty
        when (data.status.value) {
            200, 201 -> {
                bookmarkState = BookmarkState.Success(data.body())
            }

            in 400..404 -> {
                bookmarkState = BookmarkState.Error((data.body() as ApiErrorResponse).message)

            }
        }

        return bookmarkState
    }

    suspend fun getAllBookmark(idUser: String): ListBookmarkState {
        val data = client.get("${Constant.BASE_URL}/api/bookmark/$idUser") {
            contentType(ContentType.Application.Json)
            headers {
                append(
                    "api-token",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZlYnJpYW4yNjAyMjAwMUBnbWFpbC5jb20iLCJpYXQiOjE2OTg0MTIyMTksImV4cCI6MTcyOTk0ODIxOX0.ml0Vq86onfWUJnMUKdxeQCMIiP_uIpv7JbHXThp3r_U"
                )
            }


        }

        var bookmarkState: ListBookmarkState = ListBookmarkState.Empty
        when (data.status.value) {
            200, 201 -> {
                bookmarkState = ListBookmarkState.Success(data.body())
            }

            in 400..404 -> {
                bookmarkState = ListBookmarkState.Error((data.body() as ApiErrorResponse).message)
                println("ERRCode"+bookmarkState.toString())
            }
        }

        return bookmarkState
    }

    suspend fun getBookmarkById(idUser: String, bookmarkRequest: BookmarkRequest): BookmarkState {
        val data = client.get("${Constant.BASE_URL}/api/bookmark/$idUser") {
            setBody(bookmarkRequest)
            contentType(ContentType.Application.Json)
            headers {
                append(
                    "api-token",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZlYnJpYW4yNjAyMjAwMUBnbWFpbC5jb20iLCJpYXQiOjE2OTg0MTIyMTksImV4cCI6MTcyOTk0ODIxOX0.ml0Vq86onfWUJnMUKdxeQCMIiP_uIpv7JbHXThp3r_U"
                )
            }
        }

        var bookmarkState: BookmarkState = BookmarkState.Empty
        when (data.status.value) {
            200, 201 -> {
                bookmarkState = BookmarkState.Success(data.body())
            }

            in 400..404 -> {
                bookmarkState = BookmarkState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return bookmarkState
    }

}