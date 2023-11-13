package data.bookmark.model

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkRequest(
    val id_post: Int,
    val id_user: String
)