package data.bookmark.network

import kotlinx.serialization.Serializable

@Serializable
data class NotificationRequest(
    val notification: NotificationData,
    val to: String
)