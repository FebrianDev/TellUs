package data.chat

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

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

    suspend fun deleteChat(idChat: String) {
        db.collection("Chat").document(idChat).delete()
    }
}