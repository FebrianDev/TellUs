package data.post.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Int? = 0,
    val id_user: Int? = 0,
    val is_private: Boolean? = false,
    val like: Int? = 0,
    val comment: Int? = 0,
    val message: String? = "",
    val tag: String? = "",
    val token: String? = "",
    val createdAt: String? = "",
    val updatedAt: String? = ""
)