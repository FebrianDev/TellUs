package data.post.model

import kotlinx.serialization.Serializable

@Serializable
data class PostRequest(
    val id_user: String,
    val is_private: Boolean,
    val message: String,
    val tag: String,
    val token: String?
)