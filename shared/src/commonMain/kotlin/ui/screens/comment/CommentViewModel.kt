package ui.screens.comment

import data.comment.CommentSdk
import data.comment.state.CommentState
import data.comment.state.InsertCommentState
import data.comment.model.CommentRequest
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

    private val _commentState = MutableStateFlow<CommentState>(CommentState.Empty)
    val commentState: StateFlow<CommentState> get() = _commentState.asStateFlow()

    private val _insertCommentState = MutableStateFlow<InsertCommentState>(InsertCommentState.Empty)
    val insertCommentState: StateFlow<InsertCommentState> get() = _insertCommentState.asStateFlow()

    fun insertComment(commentRequest: CommentRequest) {
        _insertCommentState.value = InsertCommentState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _insertCommentState.value = sdk.insertComment(commentRequest)
        }
    }

    fun getCommentById(idPost: String) {
        _commentState.value = CommentState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _commentState.value = sdk.getCommentById(idPost)
        }
    }

    fun getReplyComment(idPost: String,idComment: String) {
        _commentState.value = CommentState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _commentState.value = sdk.getReplyComment(idPost, idComment)
        }
    }

}