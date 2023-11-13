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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {

    private val sdk = CommentSdk()

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
            _insertCommentState.value = sdk.insertComment(commentRequest)
        }
    }

    fun insertReplyComment(commentRequest: CommentReplyRequest) {
        _insertCommentState.value = Result.success(InsertCommentState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _insertCommentState.value = sdk.insertReplyComment(commentRequest)
        }
    }

    fun getCommentById(idPost: String) {
        _commentState.value = Result.success(CommentState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _commentState.value = sdk.getCommentById(idPost)
        }
    }

    fun getReplyComment(idPost: String, idComment: String) {
        _replyCommentState.value = Result.success(ReplyCommentState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _replyCommentState.value = sdk.getReplyComment(idPost, idComment)
        }
    }

}