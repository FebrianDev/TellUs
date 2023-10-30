package data.post

import data.post.network.PostApi
import data.post.state.ListPostState
import data.post.state.PostState

class PostSdk {

    private val api = PostApi()

    @Throws(Exception::class)
    suspend fun getAllPost(): ListPostState {
        return api.getAllPost()
    }

    @Throws(Exception::class)
    suspend fun getTrendingPost(): ListPostState {
        return api.getTrendingPost()
    }

    @Throws(Exception::class)
    suspend fun getPostByIdUser(id: String): ListPostState {
        return api.getPostByIdUser(id)
    }

    @Throws(Exception::class)
    suspend fun getPostByTag(tag: String): ListPostState {
        return api.getPostByTag(tag)
    }

    @Throws(Exception::class)
    suspend fun getPostById(id: String): PostState {
        return api.getPostById(id)
    }

    @Throws(Exception::class)
    suspend fun deletePost(id: String): PostState {
        return api.deletePost(id)
    }
}