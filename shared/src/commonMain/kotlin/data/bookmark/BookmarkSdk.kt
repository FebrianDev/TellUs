package data.bookmark

import data.bookmark.model.BookmarkRequest
import data.bookmark.network.BookmarkApi
import data.bookmark.state.BookmarkState

class BookmarkSdk {

    private val api = BookmarkApi()

    @Throws(Exception::class)
    suspend fun getAllBookmark(idUser: String): BookmarkState {
        return api.getAllBookmark(idUser)
    }

    @Throws(Exception::class)
    suspend fun getBookmarkById(idUser: String, bookmarkRequest: BookmarkRequest): BookmarkState {
        return api.getBookmarkById(idUser, bookmarkRequest)
    }

    @Throws(Exception::class)
    suspend fun insertBookmark(bookmarkRequest: BookmarkRequest): BookmarkState {
        return api.insertBookmark(bookmarkRequest)
    }
}