package data.post.model

import kotlinx.serialization.Serializable

@Serializable
data class PrivatePostRequest(
    val is_private: Boolean
)