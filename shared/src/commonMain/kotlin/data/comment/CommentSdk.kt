package data.comment

import data.comment.model.CommentRequest
import data.comment.network.CommentApi
import data.comment.state.CommentState
import data.comment.state.InsertCommentState

class CommentSdk {

    private val api = CommentApi()

    @Throws(Exception::class)
    suspend fun insertComment(commentRequest: CommentRequest): InsertCommentState {
        return api.insertComment(commentRequest)
    }

    @Throws(Exception::class)
    suspend fun getCommentById(idPost: String): CommentState {
        return api.getCommentById(idPost)
    }

    @Throws(Exception::class)
    suspend fun getReplyComment(idPost: String, idComment: String): CommentState {
        return api.getReplyComment(idPost, idComment)
    }
}