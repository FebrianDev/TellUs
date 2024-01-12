import androidx.compose.ui.window.ComposeUIViewController
import data.chat.ChatEntity
import data.chat.Message

actual fun getPlatformName(): String = "iOS"

actual fun sharePost() {

}

actual fun openEmail() {

}

actual fun openUrl() {

}


// private val firestore = FirebaseFirestore.getInstance()

actual fun getChat(idChat: String, onGetChat: (chat: ChatEntity) -> Unit) {
//        firestore.collection("Chat").whereEqualTo("id_chat", idChat)
//            .addSnapshotListener {value, error ->
//                if (error != null) {
//                    println("Chat Error ${error.message}")
//                    return@addSnapshotListener
//                }
//
//                (value!!.toObjects(ChatEntity::class.java) as ArrayList<ChatEntity>).forEach {
//                    onGetChat.invoke(it.message)
//                }
//            }
}

actual fun readChat(
    idChat: String,
    idSent: String
) {

}

actual fun sendChat(
    message: Message,
    idSent: String,
    idReceiver: String,
    idChat: String
) {

}

fun MainViewController() = ComposeUIViewController { App() }