package data.chat

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import utils.getDateNow

class ChatApi {

    private val db = Firebase.firestore

    suspend fun createRoom(chatEntity: ChatEntity): Result<String> {
        return try {
            val isEmpty = db.collection("Chat").where("id_sent", chatEntity.id_sent)
                .where("id_post", chatEntity.id_post)
                .where("id_receiver", chatEntity.id_receiver).get().documents.isEmpty()

            if (isEmpty) {
                val createRoom = db.collection("Chat").add(chatEntity)
                chatEntity.id_chat = createRoom.id
                createRoom.update(chatEntity)
                Result.success("Success Create Room")
            } else {
                Result.success("Failed Create Room")
            }

        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun getListRoomChat(): List<ChatEntity> {
        try {
            val chatResponse =
                db.collection("Chat").get()
            return chatResponse.documents.map {
                it.data()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getChat(idChat: String): Result<ChatState> {
        return try {
            var chatState: ChatState = ChatState.Empty
            val chatResponse =
                db.collection("Chat").where("id_chat", idChat).get()
            chatState = ChatState.Success(chatResponse.documents[0].data())
            println("GetChat")
            Result.success(chatState)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendChat(
        chatEntity: ChatEntity,
        message: Message
    ): Result<String> {
        return try {
            val listMessage = chatEntity.message
            listMessage.add(message)
            chatEntity.message = listMessage
            chatEntity.date = getDateNow()

            db.collection("Chat").document(chatEntity.id_chat)
                .update(chatEntity)

            Result.success("")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun readChat(
        idChat: String,
        idSent: String
    ) {
        try {
            val temp = ArrayList<Message>()
            val chat = db.collection("Chat").document(idChat)
                .get()

            chat.reference.snapshots.collect() {
                println("Chat3" + (it.data() as ChatEntity).toString())
            }


        }catch (e: Exception) {
            println("Chat3"+e.message)
        }

//        if (it.result.get("id_sent").toString() == idSent) {
//            it.result.reference.update("countReadSent", 0)
//        } else {
//            it.result.reference.update("countReadReceiver", 0)
//        }
//
//        it.result.toObject(ChatEntity::class.java)!!.message.forEach { data ->
//            if (!data.read && idSent != data.sender) data.read = true
//            temp.add(data)
//        }
//
//        it.result.reference.update("message", temp)

    }
}