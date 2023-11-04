package data.comment.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Int,
    val id_post: Int,
    val id_reply: Int,
    val id_user: Int,
    val message: String,
    val root: Boolean,
    val createdAt: String,
    val updatedAt: String
)