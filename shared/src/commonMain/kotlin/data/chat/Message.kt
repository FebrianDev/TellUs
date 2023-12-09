package data.chat

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    var sender: String = "",
    var prev_reply: String = "",
    var message: String = "",
    var img: String = "",
    var read: Boolean = false,
    var date: String = ""
)