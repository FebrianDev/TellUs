package data.like.model

import kotlinx.serialization.Serializable

@Serializable
data class LikeRequest(
    val id_post: Int,
    val id_user: String
)