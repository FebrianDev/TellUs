package data.like.model

import kotlinx.serialization.Serializable

@Serializable
data class LikeResponse(
    val id: Int = 0,
    val id_post: Int = 0,
    val id_user: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
)