package data.bookmark.model

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkResponse(
    var id: Int = 0,
    var id_user: String? = "",
    var id_post: Int? = 0,
    var createdAt: String? = "",
    var updatedAt: String? = ""
)