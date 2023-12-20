package data.post

import data.post.model.PostRequest
import data.post.model.PrivatePostRequest
import data.post.network.PostApi
import data.post.state.ListPostState
import data.post.state.PostState

class PostSdk {

    private val api = PostApi()

    @Throws(Exception::class)
    suspend fun getAllPost(apiToken: String): Result<ListPostState> {
        return try {
            Result.success(api.getAllPost(apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getTrendingPost(apiToken: String): Result<ListPostState> {
        return try {
            Result.success(api.getTrendingPost(apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getPostByIdUser(id: String, apiToken: String): Result<ListPostState> {
        return try {
            Result.success(api.getPostByIdUser(id, apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getPostByTag(tag: String, apiToken: String): Result<ListPostState> {
        return try {
            Result.success(api.getPostByTag(tag, apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getPostById(id: String, apiToken: String): Result<PostState> {
        return try {
            Result.success(api.getPostById(id, apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun deletePost(id: String, apiToken: String): Result<PostState> {
        return try {
            Result.success(api.deletePost(id, apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun insertPost(postRequest: PostRequest, apiToken: String): Result<PostState> {
        return try {
            Result.success(api.insertPost(postRequest, apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun insertLike(postRequest: PostRequest, apiToken: String): Result<PostState> {
        return try {
            Result.success(api.insertLike(postRequest, apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun changePrivatePost(
        privatePostRequest: PrivatePostRequest,
        idPost: String,
        apiToken: String
    ): Result<PostState> {
        return try {
            Result.success(api.changePrivatePost(privatePostRequest, idPost, apiToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}