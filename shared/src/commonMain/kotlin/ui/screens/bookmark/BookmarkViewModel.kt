package ui.screens.bookmark

import data.bookmark.BookmarkSdk
import data.bookmark.model.BookmarkRequest
import data.bookmark.state.BookmarkState
import data.bookmark.state.ListBookmarkState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import utils.KeyValueStorage
import utils.KeyValueStorageImpl

class BookmarkViewModel : ViewModel() {


    private val sdk: BookmarkSdk = BookmarkSdk()
    private val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()

    private val _bookmarkState =
        MutableStateFlow<Result<BookmarkState>>(Result.success(BookmarkState.Empty))
    val bookmarkState: StateFlow<Result<BookmarkState>> get() = _bookmarkState.asStateFlow()

    private val _listBookmarkState =
        MutableStateFlow<Result<ListBookmarkState>>(Result.success(ListBookmarkState.Empty))
    val listBookmarkState: StateFlow<Result<ListBookmarkState>> get() = _listBookmarkState.asStateFlow()

    fun insertBookmark(bookmarkRequest: BookmarkRequest) {
        _bookmarkState.value = Result.success(BookmarkState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken?.collectLatest { apiToken ->
                _bookmarkState.value = sdk.insertBookmark(bookmarkRequest, apiToken)
            }
        }
    }

    fun getBookmarkById(idUser: String, bookmarkRequest: BookmarkRequest) {
        _bookmarkState.value = Result.success(BookmarkState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken?.collectLatest { apiToken ->
                _bookmarkState.value = sdk.getBookmarkById(idUser, bookmarkRequest, apiToken)
            }
        }
    }

    fun getAllBookmark(idUser: String) {
        _listBookmarkState.value = Result.success(ListBookmarkState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken?.collectLatest { apiToken ->
                _listBookmarkState.value = sdk.getAllBookmark(idUser, apiToken)
            }

        }
    }
}