package data.bookmark

import data.bookmark.model.BookmarkRequest
import data.bookmark.network.BookmarkApi
import data.bookmark.state.BookmarkState
import data.bookmark.state.ListBookmarkState

class BookmarkSdk {

    private val api = BookmarkApi()

    @Throws(Exception::class)
    suspend fun getAllBookmark(idUser: String, apiToken: String): Result<ListBookmarkState> {
        return try {
            Result.success(api.getAllBookmark(idUser, apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getBookmarkById(
        idUser: String,
        bookmarkRequest: BookmarkRequest,
        apiToken: String
    ): Result<BookmarkState> {
        return try {
            Result.success(api.getBookmarkById(idUser, bookmarkRequest, apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun insertBookmark(
        bookmarkRequest: BookmarkRequest,
        apiToken: String
    ): Result<BookmarkState> {
        return try {
            Result.success(api.insertBookmark(bookmarkRequest, apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}