package data.bookmark

import data.bookmark.model.BookmarkRequest
import data.bookmark.network.BookmarkApi
import data.bookmark.state.BookmarkState

class BookmarkSdk {

    private val api = BookmarkApi()

    @Throws(Exception::class)
    suspend fun getAllBookmark(idUser: String): Result<BookmarkState> {
        return try {
            Result.success(api.getAllBookmark(idUser))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getBookmarkById(
        idUser: String,
        bookmarkRequest: BookmarkRequest
    ): Result<BookmarkState> {
        return try {
            Result.success(api.getBookmarkById(idUser, bookmarkRequest))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun insertBookmark(bookmarkRequest: BookmarkRequest): Result<BookmarkState> {
        return try {
            Result.success(api.insertBookmark(bookmarkRequest))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}