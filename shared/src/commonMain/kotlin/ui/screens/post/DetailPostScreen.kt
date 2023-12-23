@file:OptIn(ExperimentalMaterial3Api::class)

package ui.screens.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.bookmark.model.BookmarkRequest
import data.comment.model.CommentRequest
import data.comment.state.CommentState
import data.like.model.LikeRequest
import data.post.model.PostResponse
import data.post.state.PostState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import ui.components.DividerComposable
import ui.components.EmptyState
import ui.components.ProgressBarLoading
import ui.components.SpacerH
import ui.components.TextBodyBold
import ui.components.TextFieldComment
import ui.components.TitleHeader
import ui.screens.bookmark.BookmarkViewModel
import ui.screens.comment.CommentViewModel
import ui.screens.comment.ItemComment
import ui.screens.post.items.MyOptionPost
import ui.screens.post.items.OptionPost
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.getTime
import utils.getUid
import utils.showSnackBar

class DetailPostScreen(private val id: Int) : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val postViewModel = getViewModel(Unit, viewModelFactory { PostViewModel() })


        val commentViewModel = getViewModel(Unit, viewModelFactory { CommentViewModel() })


        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        val uid = getUid()
        var uidState by remember { mutableStateOf("") }
        uidState = uid

        LaunchedEffect(uidState){
            postViewModel.getPostById(id.toString())
            commentViewModel.getCommentById(id.toString())
        }

        val cp = LocalClipboardManager.current
        val event = OptionPostEvent()

        event.onCopyText = {
            cp.setText(AnnotatedString(it))
            showSnackBar(
                "The text has been copied successfully",
                coroutineScope,
                scaffoldState
            )
        }

        event.onDeletePost = {
            postViewModel.deletePost(it.id.toString())
            showSnackBar("Success delete post", coroutineScope, scaffoldState)
        }

        val likeViewModel = getViewModel(Unit, viewModelFactory { LikeViewModel() })
        val bookmarkViewModel = getViewModel(Unit, viewModelFactory { BookmarkViewModel() })

        var bookmarkIcon by remember { mutableStateOf(Icons.Filled.BookmarkBorder) }

        var commentCountState by remember { mutableStateOf(0) }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = scaffoldState)
            },
            containerColor = bgColor,
            contentColor = bgColor
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TitleHeader("Detail Post")
                Spacer(modifier = Modifier.height(24.dp))

                // ShowDetailPost(postViewModel, scaffoldState, coroutineScope, uid, event)

                postViewModel.postState.collectAsState().value.onSuccess {
                    when (it) {
                        is PostState.Loading -> ProgressBarLoading()
                        is PostState.Error -> showSnackBar(
                            "Something was wrong!",
                            coroutineScope,
                            scaffoldState
                        )

                        is PostState.Success -> {

                            val postResponse = it.data.data ?: PostResponse()

                            commentCountState = postResponse.comment ?: 0

                            var likeIcon by remember { mutableStateOf(Icons.Filled.FavoriteBorder) }
                            var like by remember { mutableStateOf(postResponse.like) }

                            if (postResponse.Likes?.isEmpty() == true) {
                                likeIcon = Icons.Filled.FavoriteBorder
                            } else {
                                postResponse.Likes?.forEach {
                                    likeIcon =
                                        if (it.id_post == postResponse.id && it.id_user == uid) {
                                            Icons.Filled.Favorite
                                        } else {
                                            Icons.Filled.FavoriteBorder
                                        }
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

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Card(
                                    modifier = Modifier
                                        .padding(0.dp, 4.dp, 16.dp, 4.dp)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(0.dp, 24.dp, 24.dp, 0.dp),
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
                                                androidx.compose.material.Text(
                                                    it,
                                                    color = colorPrimary,
                                                    fontSize = 14.sp,
                                                    modifier = Modifier.fillMaxWidth(0.9f)
                                                )
                                            }
                                            if (uid == postResponse.id_user) {
                                                MyOptionPost(
                                                    postResponse,
                                                    scaffoldState,
                                                    coroutineScope,
                                                    event
                                                )
                                            } else
                                                OptionPost(
                                                    postResponse,
                                                    scaffoldState,
                                                    coroutineScope,
                                                    event
                                                )
                                        }

                                        DividerComposable()

                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row {
                                                //postResponse.let { LikePost(it) }

                                                androidx.compose.material.Icon(
                                                    modifier = Modifier
                                                        .padding(top = 4.dp, start = 4.dp)
                                                        .width(24.dp)
                                                        .height(24.dp).clickable {
                                                            likeViewModel.insertLike(
                                                                LikeRequest(
                                                                    postResponse.id ?: 0,
                                                                    uid
                                                                )
                                                            )
                                                            if (likeIcon == Icons.Filled.Favorite) {
                                                                likeIcon =
                                                                    Icons.Filled.FavoriteBorder
                                                                like = like.minus(1)
                                                                showSnackBar(
                                                                    "Cancel Add Like Post",
                                                                    coroutineScope,
                                                                    scaffoldState
                                                                )
                                                            } else {
                                                                likeIcon = Icons.Filled.Favorite
                                                                like = like.plus(1)
                                                                showSnackBar(
                                                                    "Success Add Like Post",
                                                                    coroutineScope,
                                                                    scaffoldState
                                                                )
                                                            }
                                                        },

                                                    imageVector = likeIcon,
                                                    contentDescription = "Btn Like",
                                                    tint = colorPrimary
                                                )

                                                // Text Like Count
                                                androidx.compose.material.Text(
                                                    text = like.toString(),
                                                    color = colorPrimary,
                                                    modifier = Modifier
                                                        .padding(top = 4.dp, start = 6.dp),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Spacer(Modifier.width(4.dp))

                                                androidx.compose.material.Icon(
                                                    modifier = Modifier
                                                        .padding(top = 4.dp, start = 4.dp)
                                                        .width(24.dp)
                                                        .height(24.dp),

                                                    imageVector = Icons.Filled.Comment,
                                                    contentDescription = "Btn Comment",
                                                    tint = colorPrimary
                                                )

                                                // Text Like Count
                                                androidx.compose.material.Text(
                                                    text = commentCountState.toString(),
                                                    color = colorPrimary,
                                                    modifier = Modifier
                                                        .padding(top = 4.dp, start = 6.dp),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                //CommentPost(commentCountState)
                                            }

                                            androidx.compose.material.Icon(
                                                modifier = Modifier
                                                    .padding(top = 4.dp, start = 4.dp)
                                                    .width(24.dp)
                                                    .height(24.dp).clickable {
                                                        bookmarkViewModel.insertBookmark(
                                                            BookmarkRequest(
                                                                postResponse.id ?: 0,
                                                                uid
                                                            )
                                                        )
                                                        if (bookmarkIcon == Icons.Filled.Bookmark) {
                                                            bookmarkIcon =
                                                                Icons.Filled.BookmarkBorder
                                                            showSnackBar(
                                                                "Cancel Add to Bookmark",
                                                                coroutineScope,
                                                                scaffoldState
                                                            )
                                                        } else {
                                                            bookmarkIcon = Icons.Filled.Bookmark
                                                            showSnackBar(
                                                                "Success Add to Bookmark",
                                                                coroutineScope,
                                                                scaffoldState
                                                            )
                                                        }
                                                    },
                                                imageVector = bookmarkIcon,
                                                contentDescription = "Btn Bookmark",
                                                tint = colorPrimary
                                            )
                                        }
                                    }
                                }

                                androidx.compose.material.Text(
                                    text = getTime(postResponse.createdAt.toString()),
                                    color = colorPrimary,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(end = 16.dp, start = 16.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        }

                        else -> {}

                    }
                }.onFailure {
                    showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
                }

                SpacerH(16.dp)

                /*Get Comment Post*/
                commentViewModel.commentState.collectAsState().value.onSuccess {
                    when (it) {
                        is CommentState.Loading -> ProgressBarLoading()
                        is CommentState.Error -> showSnackBar(
                            "Something was wrong!",
                            coroutineScope,
                            scaffoldState
                        )

                        is CommentState.Success -> {

                            val listCommentState by remember { mutableStateOf(it.data.data?.toMutableStateList()) }

                            if (listCommentState?.isEmpty() == true) {
                                EmptyState("drawable/ic_no_comment.png", "No Comment")
                            } else {
                                LazyColumn(modifier = Modifier.fillMaxHeight(0.9f)) {
                                    items(listCommentState ?: listOf()) { item ->
                                        event.onDeleteComment = { commentResponse ->
                                            listCommentState?.remove(commentResponse)
                                            commentViewModel.deleteComment(
                                                commentResponse.id_post.toString(),
                                                commentResponse.id.toString()
                                            )
                                            commentCountState.minus(1)
                                            showSnackBar(
                                                "Success delete comment",
                                                coroutineScope,
                                                scaffoldState
                                            )
                                        }

                                        if (listCommentState?.isEmpty() == true) {
                                            EmptyState("drawable/ic_no_comment.png", "No Comment")
                                        }

                                        ItemComment(item, scaffoldState, coroutineScope, uid, event)
                                    }
                                }

                            }

                        }

                        else -> {}

                    }
                }.onFailure {
                    showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
                }

                Spacer(modifier = Modifier.weight(1f))

                /*Send Comment*/
                Row(
                    modifier = Modifier.fillMaxWidth().background(bgColor),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {

                    var textComment by remember { mutableStateOf(TextFieldValue("")) }

                    TextFieldComment(textComment) {
                        textComment = it
                    }

                    TextBodyBold(
                        text = "Send",
                        modifier = Modifier.padding(start = 4.dp, end = 16.dp).wrapContentSize()
                            .align(Alignment.CenterVertically).clickable {
                                commentViewModel.insertComment(
                                    CommentRequest(
                                        id_post = id,
                                        id_user = uid,
                                        message = textComment.text,
                                        token = "token"
                                    )
                                )

                                textComment = TextFieldValue("")
                            }
                    )
                }
            }
        }
    }


    @Composable
    fun CommentPost(comment: Int) {
        androidx.compose.material.Icon(
            modifier = Modifier
                .padding(top = 4.dp, start = 4.dp)
                .width(24.dp)
                .height(24.dp),

            imageVector = Icons.Filled.Comment,
            contentDescription = "Btn Comment",
            tint = colorPrimary
        )

        // Text Like Count
        androidx.compose.material.Text(
            text = comment.toString(),
            color = colorPrimary,
            modifier = Modifier
                .padding(top = 4.dp, start = 6.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

    }
}


