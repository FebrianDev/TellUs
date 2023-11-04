package data.comment.model

import kotlinx.serialization.Serializable

@Serializable
class CommentResponse(
    val id: Int? = 0,
    val id_post: Int? =0,
    val id_reply: Int? = 0,
    val id_user: Int? = 0,
    val message: String? = "",
    val root: Boolean? = false,
    val createdAt: String? = "",
    val updatedAt: String? = ""
)