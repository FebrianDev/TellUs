package data.post

import data.post.model.PostRequest
import data.post.network.PostApi
import data.post.state.ListPostState
import data.post.state.PostState

class PostSdk {

    private val api = PostApi()

    @Throws(Exception::class)
    suspend fun getAllPost(): Result<ListPostState> {
        return try {
            Result.success(api.getAllPost())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getTrendingPost(): Result<ListPostState> {
        return try {
            Result.success(api.getTrendingPost())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getPostByIdUser(id: String): Result<ListPostState> {
        return try {
            Result.success(api.getPostByIdUser(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getPostByTag(tag: String): Result<ListPostState> {
        return try {
            Result.success(api.getPostByTag(tag))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun getPostById(id: String): Result<PostState> {
        return try {
            Result.success(api.getPostById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun deletePost(id: String): Result<PostState> {
        return try {
            Result.success(api.deletePost(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun insertPost(postRequest: PostRequest): Result<PostState> {
        return try {
            Result.success(api.insertPost(postRequest))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun insertLike(postRequest: PostRequest): Result<PostState> {
        return try {
            Result.success(api.insertLike(postRequest))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}