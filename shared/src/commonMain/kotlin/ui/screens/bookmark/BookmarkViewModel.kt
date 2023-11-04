package ui.screens.bookmark

import data.bookmark.BookmarkSdk
import data.bookmark.state.BookmarkState
import data.bookmark.model.BookmarkRequest
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookmarkViewModel : ViewModel() {


    private val sdk: BookmarkSdk = BookmarkSdk()

    private val _bookmarkState = MutableStateFlow<BookmarkState>(BookmarkState.Empty)
    val bookmarkState: StateFlow<BookmarkState> get() = _bookmarkState.asStateFlow()

    fun insertBookmark(bookmarkRequest: BookmarkRequest) {
        _bookmarkState.value = BookmarkState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _bookmarkState.value = sdk.insertBookmark(bookmarkRequest)
        }
    }

    fun getBookmarkById(idUser: String, bookmarkRequest: BookmarkRequest) {
        _bookmarkState.value = BookmarkState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _bookmarkState.value = sdk.getBookmarkById(idUser, bookmarkRequest)
        }
    }

    fun getAllBookmark(idUser: String) {
        _bookmarkState.value = BookmarkState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _bookmarkState.value = sdk.getAllBookmark(idUser)
        }
    }
}