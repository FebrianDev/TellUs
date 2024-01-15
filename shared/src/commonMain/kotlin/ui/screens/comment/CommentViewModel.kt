package ui.screens.comment

import data.comment.CommentSdk
import data.comment.model.CommentReplyRequest
import data.comment.model.CommentRequest
import data.comment.state.CommentState
import data.comment.state.InsertCommentState
import data.comment.state.ReplyCommentState
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

class CommentViewModel : ViewModel() {

    private val sdk = CommentSdk()
    private val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()

    private val _commentState =
        MutableStateFlow<Result<CommentState>>(Result.success(CommentState.Empty))
    val commentState: StateFlow<Result<CommentState>> get() = _commentState.asStateFlow()

    private val _replyCommentState =
        MutableStateFlow<Result<ReplyCommentState>>(Result.success(ReplyCommentState.Empty))
    val replyCommentState: StateFlow<Result<ReplyCommentState>> get() = _replyCommentState.asStateFlow()

    private val _insertCommentState =
        MutableStateFlow<Result<InsertCommentState>>(Result.success(InsertCommentState.Empty))
    val insertCommentState: StateFlow<Result<InsertCommentState>> get() = _insertCommentState.asStateFlow()

    fun insertComment(commentRequest: CommentRequest) {
        _insertCommentState.value = Result.success(InsertCommentState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest { apiToken ->
                sdk.insertComment(commentRequest, apiToken)
                delay(300)
                getCommentById(commentRequest.id_post.toString())
            }
        }
    }

    fun insertReplyComment(commentRequest: CommentReplyRequest) {
        _insertCommentState.value = Result.success(InsertCommentState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest { apiToken ->
                sdk.insertReplyComment(commentRequest, apiToken)
                delay(300)
                getReplyComment(
                    commentRequest.id_post.toString(),
                    commentRequest.id_reply.toString()
                )
            }
        }
    }

    fun getCommentById(idPost: String) {
        _commentState.value = Result.success(CommentState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest { apiToken ->
                _commentState.value = sdk.getCommentById(idPost, apiToken)
            }
        }
    }

    fun getReplyComment(idPost: String, idComment: String) {
        _replyCommentState.value = Result.success(ReplyCommentState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken/**/.collectLatest { apiToken ->
                _replyCommentState.value = sdk.getReplyComment(idPost, idComment, apiToken)
            }
        }
    }

    fun deleteComment(idPost: String, idComment: String) {
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest { apiToken ->
                sdk.deleteComment(idPost, idComment, apiToken)
            }
        }

    }

}