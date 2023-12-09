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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val postSdk = PostSdk()

    private val _listPostState =
        MutableStateFlow<Result<ListPostState>>(Result.success(ListPostState.Empty))
    val listPostState: StateFlow<Result<ListPostState>> get() = _listPostState.asStateFlow()

    private val _postState = MutableStateFlow<Result<PostState>>(Result.success(PostState.Empty))
    val postState: StateFlow<Result<PostState>> get() = _postState.asStateFlow()

    private val _insertPostState =
        MutableStateFlow<Result<PostState>>(Result.success(PostState.Empty))
    val insertPostState: StateFlow<Result<PostState>> get() = _insertPostState.asStateFlow()

    fun getAllPost() {
        _listPostState.value = Result.success(ListPostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _listPostState.value = postSdk.getAllPost()
        }
    }

    fun getTrending() {
        _listPostState.value = Result.success(ListPostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _listPostState.value = postSdk.getTrendingPost()
        }
    }

    fun getPostByIdUser(id: String) {
        _listPostState.value = Result.success(ListPostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _listPostState.value = postSdk.getPostByIdUser(id)
        }
    }

    fun getPostByTag(tag: String) {
        _listPostState.value = Result.success(ListPostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _listPostState.value = postSdk.getPostByTag(tag)
        }
    }

    fun getPostById(id: String) {
        _postState.value = Result.success(PostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _postState.value = postSdk.getPostById(id)
        }
    }

    fun deletePost(id: String) {
      //  _postState.value = Result.success(PostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
             postSdk.deletePost(id)
        }
    }

    fun insertPost(postRequest: PostRequest) {
        _insertPostState.value = Result.success(PostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _insertPostState.value = postSdk.insertPost(postRequest)
        }
    }

    fun changePrivatePost(privatePostRequest: PrivatePostRequest, idPost: String) {
        _postState.value = Result.success(PostState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _postState.value = postSdk.changePrivatePost(privatePostRequest, idPost)
        }
    }
}
