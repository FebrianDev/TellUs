package data.comment

import data.comment.model.CommentReplyRequest
import data.comment.model.CommentRequest
import data.comment.network.CommentApi
import data.comment.state.CommentState
import data.comment.state.InsertCommentState
import data.comment.state.ReplyCommentState
import data.post.state.PostState

class CommentSdk {

    private val api = CommentApi()

    @Throws(Exception::class)
    suspend fun insertComment(commentRequest: CommentRequest): Result<InsertCommentState> {
        return try {
            Result.success(api.insertComment(commentRequest))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    @Throws(Exception::class)
//    suspend fun insertComment(commentRequest: CommentRequest): Result<String> {
//        return api.insertComment(commentRequest)
//    }

    @Throws(Exception::class)
    suspend fun insertReplyComment(commentRequest: CommentReplyRequest): Result<InsertCommentState> {
        return try {
            Result.success(api.insertReplyComment(commentRequest))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getCommentById(idPost: String): Result<CommentState> {
        return try {
            Result.success(api.getCommentById(idPost))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getReplyComment(idPost: String, idComment: String): Result<ReplyCommentState> {
        return try {
            Result.success(api.getReplyComment(idPost, idComment))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun deleteComment(idPost: String, idComment: String): Result<CommentState> {
        return try {
            Result.success(api.deleteComment(idPost, idComment))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}