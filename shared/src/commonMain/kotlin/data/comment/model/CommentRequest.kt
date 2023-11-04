package data.comment.model

@kotlinx.serialization.Serializable
data class CommentRequest(
    val id_post: Int? = 0,
    val id_reply: Int? = 0,
    val id_user: Int? = 0,
    val message: String? = "",
    val root: Boolean? = false,
    val token: String? = ""
)