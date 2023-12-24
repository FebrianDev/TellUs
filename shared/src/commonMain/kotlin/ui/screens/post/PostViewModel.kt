package ui.screens.post

import data.post.PostSdk
import data.post.model.PostRequest
import data.post.model.PrivatePostRequest
import data.post.state.ListPostState
import data.post.state.PostState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import utils.KeyValueStorage
import utils.KeyValueStorageImpl

class PostViewModel : ViewModel() {

    private val postSdk = PostSdk()
    private val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()

    private val _listPostState =
        MutableStateFlow<Result<ListPostState>>(Result.success(ListPostState.Empty))
    val listPostState: StateFlow<Result<ListPostState>> get() = _listPostState.asStateFlow()

    private val _listBestPostState =
        MutableStateFlow<Result<ListPostState>>(Result.success(ListPostState.Empty))
    val listBestPostState: StateFlow<Result<ListPostState>> get() = _listBestPostState.asStateFlow()

    private val _listMyPostState =
        MutableStateFlow<Result<ListPostState>>(Result.success(ListPostState.Empty))
    val listMyPostState: StateFlow<Result<ListPostState>> get() = _listMyPostState.asStateFlow()

    private val _postState = MutableStateFlow<Result<PostState>>(Result.success(PostState.Empty))
    val postState: StateFlow<Result<PostState>> get() = _postState.asStateFlow()

    private val _insertPostState =
        MutableStateFlow<Result<PostState>>(Result.success(PostState.Empty))
    val insertPostState: StateFlow<Result<PostState>> get() = _insertPostState.asStateFlow()

    fun getAllPost(tag: String = "") {
        _listPostState.value = Result.success(ListPostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest { apiToken ->
                delay(100)
                _listPostState.value = if (tag.isEmpty()) postSdk.getAllPost(apiToken)
                else postSdk.getPostByTag(tag, apiToken)
            }
        }
    }

    fun getTrending() {
        _listBestPostState.value = Result.success(ListPostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest { apiToken ->
                _listBestPostState.value = postSdk.getTrendingPost(apiToken)
            }
        }
    }

    fun getPostByIdUser(id: String) {
        _listMyPostState.value = Result.success(ListPostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest { apiToken ->
                _listMyPostState.value = postSdk.getPostByIdUser(id, apiToken)
            }
        }
    }

    fun getPostById(id: String) {
        _postState.value = Result.success(PostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest { apiToken ->
                _postState.value = postSdk.getPostById(id, apiToken)
            }
        }
    }

    fun deletePost(id: String) {
        //  _postState.value = Result.success(PostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest { apiToken ->
                postSdk.deletePost(id, apiToken)
            }
        }
    }

    fun insertPost(postRequest: PostRequest) {
        _insertPostState.value = Result.success(PostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest { apiToken ->
                _insertPostState.value = postSdk.insertPost(postRequest, apiToken)
            }
        }
    }

    fun changePrivatePost(privatePostRequest: PrivatePostRequest, idPost: String) {
        _postState.value = Result.success(PostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest { apiToken ->
                _postState.value = postSdk.changePrivatePost(privatePostRequest, idPost, apiToken)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
