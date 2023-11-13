package data.comment.model

@kotlinx.serialization.Serializable
data class CommentRequest(
    val id_post: Int? = 0,
    val id_user: String? = "",
    val message: String? = "",
    val token: String? = "",

)