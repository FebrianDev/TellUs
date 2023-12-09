package data.chat

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

class ChatApi {

    private val db = Firebase.firestore

    suspend fun createRoom(chatEntity: ChatEntity): Result<String> {
        return try {
            val isEmpty = db.collection("Chat").where("id_sent", chatEntity.id_sent)
                .where("id_tweet", chatEntity.id_post)
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
            Result.success(chatState)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendChat(
        chatEntity: ChatEntity,
        message: Message,
        date: String
    ): Result<String> {
        return try {
            val listMessage = chatEntity.message
            listMessage.add(message)
            chatEntity.message = listMessage
            chatEntity.date = date

            db.collection("Chat").document(chatEntity.id_chat)
                .update(chatEntity)

            Result.success("")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}