import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import data.chat.ChatEntity
import data.chat.Message
import utils.getDateNow

actual fun getPlatformName(): String = "Android"


private var c: Context? = null

actual fun sharePost() {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/FebrianDev")
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    c?.startActivity(Intent.createChooser(intent, "Share link"))
}

actual fun openEmail() {
    c?.startActivity(Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:febrian26022001@gmail.com")
    })
}

actual fun openUrl() {
    c?.startActivity(Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://linktr.ee/febrian2001")
    })
}

private val firestore = FirebaseFirestore.getInstance()

actual fun getChat(idChat: String, onGetChat: (chat: ChatEntity) -> Unit) {
    firestore.collection("Chat").whereEqualTo("id_chat", idChat)
        .addSnapshotListener { value, error ->
            if (error != null) {
                println("Chat Error ${error.message}")
                return@addSnapshotListener
            }

            val chat =
                (value!!.toObjects(ChatEntity::class.java) as ArrayList<ChatEntity>).map { it }
            if (chat.isNotEmpty())
                onGetChat.invoke(chat[0])
        }
}

actual fun readChat(
    idChat: String,
    idSent: String
) {
    val temp = java.util.ArrayList<Message>()
    firestore.collection("Chat").document(idChat)
        .get().addOnCompleteListener {

            if (it.result.get("id_sent").toString() == idSent) {
                it.result.reference.update("countReadSent", 0)
            } else {
                it.result.reference.update("countReadReceiver", 0)
            }

            it.result.toObject(ChatEntity::class.java)!!.message.forEach { data ->
                if (!data.read && idSent != data.sender) data.read = true
                temp.add(data)
            }

            it.result.reference.update("message", temp)
        }
}

actual fun sendChat(
    message: Message,
    idSent: String,
    idReceiver: String,
    idChat: String,
) {
    firestore.collection("Chat").whereEqualTo("id_chat", idChat).get().addOnCompleteListener {
        firestore.collection("Chat").document(it.result.documents[0].id)
            .update("message", FieldValue.arrayUnion(message), "date", getDateNow())

        if (message.sender == it.result.documents[0].get("id_sent").toString()) {
            it.result.documents[0].reference.update(
                "countReadReceiver",
                it.result.documents[0].get("countReadReceiver").toString().toInt() + 1
            )
        } else {
            it.result.documents[0].reference.update(
                "countReadSent",
                it.result.documents[0].get("countReadSent").toString().toInt() + 1
            )
        }
    }
}

@Composable
fun MainView(context: Context) {
    c = context
    App()
}
