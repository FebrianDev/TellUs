package ui.screens.post

import data.comment.model.CommentResponse
import data.post.model.PostResponse

data class OptionPostEvent(
    var onCopyText: (text: String) -> Unit = {},
    var onShareLink: (text: String) -> Unit = {},
    var onChangePrivatePost: (isPrivate: Boolean) -> Unit = {},
    var onDeletePost: (post: PostResponse) -> Unit = {},
    var onDeleteComment:(comment:CommentResponse) -> Unit = {}
)