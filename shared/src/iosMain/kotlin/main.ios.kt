import androidx.compose.ui.window.ComposeUIViewController
import data.chat.ChatEntity
import data.chat.Message
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.cinterop.BetaInteropApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import platform.Foundation.NSURL.Companion.URLWithString
import platform.MessageUI.MFMailComposeViewController
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual fun getPlatformName(): String = "iOS"

actual fun openEmail() {

    URLWithString("mailto:febrian26022001@gmail.com")?.let {
        UIApplication.sharedApplication.canOpenURL(
            it
        )
    }

}

actual fun openUrl() {

    URLWithString("https://linktr.ee/febrian2001")?.let {
        UIApplication.sharedApplication.openURL(
            it
        )
    }

}

private val firestore = Firebase.firestore

@OptIn(BetaInteropApi::class)
actual fun getChat(idChat: String, onGetChat: (chat: ChatEntity) -> Unit) {
    firestore.collection("Chat").document(idChat).ios
        .addSnapshotListener { value, error ->
            if (error != null) {
                println("Chat Error ${error.domain}")
                return@addSnapshotListener
            }

            val data = value?.data()

            val chat = ChatEntity(
                id_chat = data?.get("id_chat") as String,
                id_sent = data["id_sent"] as? String ?: "",
                id_receiver = data["id_receiver"] as? String ?: "",
                id_post = data["id_post"] as? String ?: "",
                post_message = data["post_message"] as? String ?: "",
                name = data["name"] as? String ?: "",
                message = mapMessageList(data["message"]),  // Example: assuming "message" is a list in Firestore
                countReadSent = (data["countReadSent"] as? Long)?.toInt() ?: 0,
                countReadReceiver = (data["countReadReceiver"] as? Long)?.toInt() ?: 0,
                date = data["date"] as? String ?: "",
                token_sent = data["token_sent"] as? String ?: "",
                token_receiver = data["token_receiver"] as? String ?: ""
            )

            onGetChat.invoke(chat)
        }

}

private fun mapMessageList(messages: Any?): ArrayList<Message> {
    if (messages is List<*>) {
        return messages.mapNotNull { it as? Map<String, Any> }
            .map { mapMessage(it) }
            .toCollection(ArrayList())
    }
    return ArrayList()
}

private fun mapMessage(messageData: Map<String, Any>): Message {
    return Message(
        sender = messageData["sender"] as? String ?: "",
        prev_reply = messageData["prev_reply"] as? String ?: "",
        message = messageData["message"] as? String ?: "",
        img = messageData["img"] as? String ?: "",
        read = messageData["read"] as? Boolean ?: false,
        date = messageData["date"] as? String ?: ""
    )
}

@OptIn(DelicateCoroutinesApi::class)
actual fun readChat(
    idChat: String,
    idSent: String
) {
    val temp = ArrayList<Message>()
    firestore.collection("Chat").document(idChat).ios.getDocumentWithCompletion { value, error ->

        if (error != null) {
            println("Chat Error ${error.domain}")
            return@getDocumentWithCompletion
        }

        val data = value?.data()

        if (data?.get("id_sent").toString() == idSent) {
            GlobalScope.launch {
                firestore.collection("Chat").document(idChat).update("countReadSent" to 0)
            }
        } else {
            GlobalScope.launch {
                firestore.collection("Chat").document(idChat).update("countReadReceiver" to 0)
            }
        }

        mapMessageList(data?.get("message")).forEach {
            if (!it.read && idSent != it.sender) it.read = true
            temp.add(it)
        }

        GlobalScope.launch {
            firestore.collection("Chat").document(idChat).update("message" to temp)
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
actual fun sendChat(
    message: Message,
    idSent: String,
    idReceiver: String,
    idChat: String,
) {

    firestore.collection("Chat").document(idChat).ios.getDocumentWithCompletion { value, error ->

        if (error != null) {
            println("Chat Error ${error.domain}")
            return@getDocumentWithCompletion
        }

        val list = mapMessageList(value?.data()?.get("message"))
        list.add(message)
        GlobalScope.launch {
            firestore.collection("Chat").document(idChat).update("message" to list)
        }

        if (message.sender == value?.data()?.get("id_sent").toString()) {
            GlobalScope.launch {
                firestore.collection("Chat").document(idChat).update(
                    "countReadReceiver" to
                            value?.data()?.get("countReadReceiver").toString().toInt() + 1
                )
            }
        } else {
            GlobalScope.launch {
                firestore.collection("Chat").document(idChat).update(
                    "countReadSent" to
                            value?.data()?.get("countReadSent").toString().toInt() + 1
                )
            }
        }
    }
}

fun MainViewController() = ComposeUIViewController { App() }