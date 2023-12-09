package data.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatEntity(
    var id_chat: String = "",
    var id_sent: String = "",
    var id_receiver: String = "",
    var id_post: String = "",
    var post_message: String = "",
    var name: String = "",
    var message: ArrayList<Message> = ArrayList(),
    var countReadSent: Int = 0,
    var countReadReceiver: Int = 0,
    var date: String = "",
    var token: String = ""
)