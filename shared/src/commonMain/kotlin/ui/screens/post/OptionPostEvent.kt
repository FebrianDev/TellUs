package ui.screens.post

import androidx.compose.ui.graphics.vector.ImageVector
import data.comment.model.CommentResponse
import data.comment.model.ReplyCommentResponse
import data.post.model.PostResponse

data class OptionPostEvent(
    var onCopyText: (text: String) -> Unit = {},
    var onShareLink: (text: String) -> Unit = {},
    var onChangePrivatePost: (isPrivate: Boolean) -> Unit = {},
    var onDeletePost: (post: PostResponse) -> Unit = {},
    var onDeleteComment: (comment: CommentResponse) -> Unit = {},
    var onDeleteReplyComment: (comment: ReplyCommentResponse) -> Unit = {},
    var onSendPrivateChat: () -> Unit = {},

    var onLikePost: (state:Boolean) -> Unit = {},
    var onBookmarkPost: () -> Unit = {}
)