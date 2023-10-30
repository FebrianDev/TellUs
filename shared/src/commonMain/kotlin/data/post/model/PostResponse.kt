package data.post.model

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse (
    val id: Int,
    val id_user: Int,
    val is_private: Boolean,
    val like: Int,
    val comment: Int,
    val message: String,
    val tag: String,
    val token: String,
    val createdAt: String,
    val updatedAt: String
)