package data.comment.model

import kotlinx.serialization.Serializable

@Serializable
class CommentReplyRequest(
    val id_post: Int? = 0,
    val id_reply: Int? = 0,
    val id_user: String? = "",
    val prev_message:String? = "",
    val message: String? = "",
    val is_root: Boolean? = false,
    val token:String? = ""
)