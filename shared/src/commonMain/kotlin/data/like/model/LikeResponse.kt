package data.like.model

import kotlinx.serialization.Serializable

@Serializable
data class LikeResponse(
    val id: Int,
    val id_post: Int,
    val id_user: Int,
    val createdAt: String,
    val updatedAt: String
)