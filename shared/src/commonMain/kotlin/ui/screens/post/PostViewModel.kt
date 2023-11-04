package ui.screens.post


import data.post.PostSdk
import data.post.model.PostRequest
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

    private val _listPostState = MutableStateFlow<ListPostState>(ListPostState.Empty)
    val listPostState: StateFlow<ListPostState> get() = _listPostState.asStateFlow()

    private val _postState = MutableStateFlow<PostState>(PostState.Empty)
    val postState: StateFlow<PostState> get() = _postState.asStateFlow()

    fun getAllPost() {
        _listPostState.value = ListPostState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _listPostState.value = postSdk.getAllPost()
        }
    }

    fun getTrending() {
        _listPostState.value = ListPostState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _listPostState.value = postSdk.getTrendingPost()
        }
    }

    fun getPostByIdUser(id: String) {
        _listPostState.value = ListPostState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _listPostState.value = postSdk.getPostByIdUser(id)
        }
    }

    fun getPostByTag(tag: String) {
        _listPostState.value = ListPostState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _listPostState.value = postSdk.getPostByTag(tag)
        }
    }

    fun getPostById(id: String) {
        _postState.value = PostState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postState.value = postSdk.getPostById(id)
        }
    }

    fun deletePost(id: String) {
        _postState.value = PostState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postState.value = postSdk.deletePost(id)
        }
    }

    fun insertPost(postRequest: PostRequest) {
        _postState.value = PostState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _postState.value = postSdk.insertPost(postRequest)
        }
    }
}
