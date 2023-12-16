package ui.screens.post.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.bookmark.model.BookmarkRequest
import data.like.model.LikeRequest
import data.post.model.PostResponse
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import ui.screens.bookmark.BookmarkViewModel
import ui.screens.post.DetailPostScreen
import ui.screens.post.LikeViewModel
import ui.screens.post.OptionPostEvent
import ui.screens.post.PostViewModel
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.getTime
import utils.getUid
import utils.showSnackBar

@Composable
fun ItemPostBookmark(
    postResponse: PostResponse,
    uid:String,
    coroutineScope: CoroutineScope,
    scaffoldState: SnackbarHostState,
    event : OptionPostEvent,
    onBookmarkPost : () -> Unit = {}
) {

    val navigator = LocalNavigator.currentOrThrow

    val likeViewModel = getViewModel(Unit, viewModelFactory { LikeViewModel() })
    val bookmarkViewModel = getViewModel(Unit, viewModelFactory { BookmarkViewModel() })

    val likeIcon = remember { mutableStateOf(Icons.Filled.FavoriteBorder) }
    var like by remember { mutableStateOf(postResponse.like) }

    var bookmarkIcon by remember { mutableStateOf(Icons.Filled.BookmarkBorder) }

    if (postResponse.Likes?.isEmpty() == true) {
        likeIcon.value = Icons.Filled.FavoriteBorder
    } else {

        val likedPosts = postResponse.Likes?.filter {
            it.id_post == postResponse.id && it.id_user == uid
        }

        likeIcon.value = if (!likedPosts.isNullOrEmpty()) {
            Icons.Filled.Favorite
        } else {
            Icons.Filled.FavoriteBorder
        }
    }

    if (postResponse.Bookmarks?.isEmpty() == true) {
        bookmarkIcon = Icons.Filled.BookmarkBorder
    } else {
        postResponse.Bookmarks?.forEach {
            bookmarkIcon =
                if (it.id_post == postResponse.id && it.id_user == uid) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder
        }
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = getTime(postResponse.createdAt.toString()),
            color = colorPrimary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterVertically)
        )

        Card(
            modifier = Modifier
                .padding(16.dp, 4.dp, 0.dp, 4.dp)
                .fillMaxWidth()
                .heightIn(min = 96.dp, max = 256.dp)
                .clickable {
                    navigator.push(DetailPostScreen(postResponse.id ?: 0))
                },
            shape = RoundedCornerShape(24.dp, 0.dp, 0.dp, 24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = bgColor)

        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    postResponse.message?.let {
                        Text(
                            it,
                            color = colorPrimary,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(0.9f)
                        )
                    }

                    if (uid == postResponse.id_user) {
                        MyOptionPost(postResponse, scaffoldState, coroutineScope, event)
                    } else
                        OptionPost(postResponse, scaffoldState, coroutineScope, event)
                }

                Divider(
                    color = colorPrimary,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Like
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row {
                        //LikePost(postResponse, likeViewModel)
                        Icon(
                            modifier = Modifier
                                .padding(top = 4.dp, start = 4.dp)
                                .width(24.dp)
                                .height(24.dp).clickable {
                                    likeViewModel.insertLike(LikeRequest(postResponse.id ?: 0, uid))
                                    if (likeIcon.value == Icons.Filled.Favorite) {
                                        likeIcon.value = Icons.Filled.FavoriteBorder
                                        like = like.minus(1)
                                        showSnackBar(
                                            "Cancel Add Like Post",
                                            coroutineScope,
                                            scaffoldState
                                        )
                                    } else {
                                        likeIcon.value = Icons.Filled.Favorite
                                        like = like.plus(1)
                                        showSnackBar(
                                            "Success Add Like Post",
                                            coroutineScope,
                                            scaffoldState
                                        )
                                    }
                                },
                            imageVector = likeIcon.value,
                            contentDescription = "Btn Like",
                            tint = colorPrimary
                        )

                        // Text Like Count
                        Text(
                            text = like.toString(),
                            color = colorPrimary,
                            modifier = Modifier
                                .padding(top = 4.dp, start = 6.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.width(4.dp))
                        postResponse.comment?.let { CommentPost(it) }
                    }

                    Icon(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 4.dp)
                            .width(24.dp)
                            .height(24.dp).clickable (onClick = onBookmarkPost),

                        imageVector = bookmarkIcon,
                        contentDescription = "Btn Bookmark",
                        tint = colorPrimary
                    )
                }
            }
        }
    }
}

