package data.comment.model

import kotlinx.serialization.Serializable

@Serializable
class CommentResponse(
    val id: Int = 0,
    val id_post: Int =0,
    val id_reply: Int = 0,
    val id_user: String = "",
    val message: String = "",
    val root: Boolean = false,
    val token:String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val ReplyComments : List<ReplyCommentResponse> = listOf()
)