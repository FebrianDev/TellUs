package data.chat

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import getChat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import utils.getDateNow

class ChatApi {

    private val db = Firebase.firestore

    suspend fun createRoom(chatEntity: ChatEntity): Result<String> {
        return try {
            val room = db.collection("Chat").where("id_sent", chatEntity.id_sent)
                .where("id_post", chatEntity.id_post)
                .where("id_receiver", chatEntity.id_receiver).get().documents

            if (room.isEmpty()) {
                val createRoom = db.collection("Chat").add(chatEntity)
                chatEntity.id_chat = createRoom.id
                createRoom.update(chatEntity)
                Result.success(chatEntity.id_chat)
            } else {
                Result.success(room[0].id)
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

    fun getChat(idChat: String): ChatState {

        var chatState: ChatState = ChatState.Empty
//        val chatResponse =
//            db.collection("Chat").document(idChat).get()
//        chatState = ChatState.Success(chatResponse.reference.snapshots.map {
//
//            it.data() as ChatEntity
//        }.first())

        getChat(idChat){
            chatState = ChatState.Success(it)
            println("GetChat1 "+it)
        }

        return chatState

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

            val chat = db.collection("Chat").document(chatEntity.id_chat)

            chat.update(chatEntity)

//            chat.get().reference.snapshots.collect() {
//                val data = it.data() as ChatEntity
//                if (message.sender == data.id_sent.toString()) {
//                    it.result.documents[0].reference.update(
//                        "countReadReceiver",
//                        it.result.documents[0].get("countReadReceiver").toString().toInt() + 1
//                    )
//                } else {
//                    it.result.documents[0].reference.update(
//                        "countReadSent",
//                        it.result.documents[0].get("countReadSent").toString().toInt() + 1
//                    )
//                }
//            }


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

        } catch (e: Exception) {
            println(e.message)
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

    suspend fun deleteChat(idChat: String) {
        db.collection("Chat").document(idChat).delete()
    }
}