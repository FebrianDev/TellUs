package data.bookmark.network

import kotlinx.serialization.Serializable

@Serializable
data class NotificationData(
    val title: String,
    val body: String
)