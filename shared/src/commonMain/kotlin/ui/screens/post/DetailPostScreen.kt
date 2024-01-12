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
import androidx.compose.material.Icon
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import data.bookmark.network.NotificationData
import data.bookmark.network.NotificationRequest
import data.chat.ChatEntity
import data.comment.model.CommentRequest
import data.comment.state.CommentState
import data.like.model.LikeRequest
import data.post.model.PostResponse
import data.post.state.PostState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import getPlatformName
import kotlinx.coroutines.CoroutineScope
import ui.components.DividerComposable
import ui.components.EmptyState
import ui.components.ProgressBarLoading
import ui.components.SpacerH
import ui.components.SpacerW
import ui.components.TextBodyBold
import ui.components.TextFieldComment
import ui.components.TextSmallBold
import ui.components.TitleHeader
import ui.screens.NotificationViewModel
import ui.screens.bookmark.BookmarkViewModel
import ui.screens.chat.ChatRoomScreen
import ui.screens.chat.ChatViewModel
import ui.screens.comment.CommentViewModel
import ui.screens.comment.ItemComment
import ui.screens.post.items.MyOptionPost
import ui.screens.post.items.OptionPost
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.KeyValueStorageImpl
import utils.generatedFakeName
import utils.getDateNow
import utils.getIdUser
import utils.getTime
import utils.showSnackBar

class DetailPostScreen(private val id: Int) : Screen {

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val postViewModel = getViewModel(Unit, viewModelFactory { PostViewModel() })
        val commentViewModel = getViewModel(Unit, viewModelFactory { CommentViewModel() })
        val notificationViewModel = getViewModel(Unit, viewModelFactory { NotificationViewModel() })
        val chatViewModel = getViewModel(Unit, viewModelFactory { ChatViewModel() })

        val keyValueStorage = KeyValueStorageImpl()

        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        val uid = getIdUser()

        LaunchedEffect(uid) {
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

        var commentCountState by rememberSaveable { mutableStateOf(0) }

        var postData by remember { mutableStateOf(PostResponse()) }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = scaffoldState)
            },
            containerColor = bgColor,
            contentColor = bgColor
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                TitleHeader("Detail Post", navigator)

                SpacerH(24.dp)

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
                            postData = postResponse
                            commentCountState = postResponse.comment

                            var sendChat by remember { mutableStateOf(false) }

                            var chat by remember { mutableStateOf(ChatEntity()) }

                            val chatEntity = ChatEntity(
                                id_chat = "",
                                id_sent = utils.keyValueStorage.idUser,
                                id_receiver = postResponse.id_user,
                                id_post = postResponse.id.toString(),
                                post_message = postResponse.message,
                                name = generatedFakeName(),
                                message = ArrayList(),
                                countReadSent = 0,
                                countReadReceiver = 0,
                                date = getDateNow(),
                                token_sent = utils.keyValueStorage.fcmToken,
                                token_receiver = postResponse.token
                            )

                            event.onSendPrivateChat = {
                                sendChat = true
                                chat = it
                                chatViewModel.createRoom(
                                    it
                                )
                            }

                            if (sendChat) {
                                chatViewModel.createRoom.collectAsState().value.onSuccess {
                                    if (it.isNotEmpty()) {
                                        chat.id_chat = it
                                        navigator.push(ChatRoomScreen(chat))
                                    }
                                }.onFailure {
                                    showSnackBar(
                                        it.message.toString(),
                                        coroutineScope,
                                        scaffoldState
                                    )
                                }
                            }

                            var like by rememberSaveable { mutableStateOf(postResponse.like) }

                            var likeState by rememberSaveable {
                                mutableStateOf(if (postResponse.Likes.isEmpty()) {
                                    false
                                } else {
                                    val likedPosts = postResponse.Likes.filter { like ->
                                        like.id_post == postResponse.id && like.id_user == uid
                                    }
                                    likedPosts.isNotEmpty()
                                })
                            }
                            var bookmarkState by rememberSaveable {
                                mutableStateOf(if (postResponse.Bookmarks.isEmpty()) {
                                    false
                                } else {
                                    val listBookmark =
                                        postResponse.Bookmarks.filter { bookmark -> bookmark.id_post == postResponse.id && bookmark.id_user == uid }
                                    listBookmark.isNotEmpty()
                                })
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Card(
                                    modifier = Modifier
                                        .padding(0.dp, 4.dp, 0.dp, 4.dp)
                                        .fillMaxWidth(0.85f),
                                    shape = RoundedCornerShape(0.dp, 24.dp, 24.dp, 0.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)

                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {

                                            androidx.compose.material.Text(
                                                postResponse.message,
                                                color = colorPrimary,
                                                fontSize = 14.sp,
                                                modifier = Modifier.fillMaxWidth(0.9f)
                                            )

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
                                                    chatEntity,
                                                    event
                                                )
                                        }

                                        TextSmallBold(postResponse.tag)

                                        DividerComposable()

                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row {
                                                Icon(
                                                    modifier = Modifier
                                                        .padding(top = 4.dp, start = 4.dp)
                                                        .width(24.dp)
                                                        .height(24.dp).clickable {
                                                            likeViewModel.insertLike(
                                                                LikeRequest(
                                                                    postResponse.id,
                                                                    uid
                                                                )
                                                            )
                                                            likeState = !likeState
                                                            like = if (likeState) like.plus(1)
                                                            else like.minus(1)

                                                            if (likeState)
                                                                showSnackBar(
                                                                    "Success Add Like Post",
                                                                    coroutineScope,
                                                                    scaffoldState
                                                                )
                                                            else
                                                                showSnackBar(
                                                                    "Cancel Add Like Post",
                                                                    coroutineScope,
                                                                    scaffoldState
                                                                )
                                                        },
                                                    imageVector = if (likeState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
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

                                                SpacerW(4.dp)

                                                Icon(
                                                    modifier = Modifier
                                                        .padding(top = 4.dp, start = 4.dp)
                                                        .width(24.dp)
                                                        .height(24.dp),

                                                    imageVector = Icons.Filled.Comment,
                                                    contentDescription = "Btn Comment",
                                                    tint = colorPrimary
                                                )

                                                // Text Comment Count
                                                androidx.compose.material.Text(
                                                    text = commentCountState.toString(),
                                                    color = colorPrimary,
                                                    modifier = Modifier
                                                        .padding(top = 4.dp, start = 6.dp),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            Icon(
                                                modifier = Modifier
                                                    .padding(top = 4.dp, start = 4.dp)
                                                    .width(24.dp)
                                                    .height(24.dp).clickable {
                                                        bookmarkViewModel.insertBookmark(
                                                            BookmarkRequest(postResponse.id, uid)
                                                        )

                                                        bookmarkState = !bookmarkState

                                                        if (bookmarkState) {
                                                            showSnackBar(
                                                                "Success Add to Bookmark",
                                                                coroutineScope,
                                                                scaffoldState
                                                            )
                                                        } else {
                                                            showSnackBar(
                                                                "Cancel Add to Bookmark",
                                                                coroutineScope,
                                                                scaffoldState
                                                            )
                                                        }

                                                    },

                                                imageVector = if (bookmarkState) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                                contentDescription = "Btn Bookmark",
                                                tint = colorPrimary
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = getTime(postResponse.createdAt),
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

                                            commentCountState = commentCountState.minus(1)

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

                                if (textComment.text.isEmpty()) return@clickable

                                commentViewModel.insertComment(
                                    CommentRequest(
                                        id_post = id,
                                        id_user = uid,
                                        message = textComment.text,
                                        token = keyValueStorage.observableFCMToken
                                    )
                                )

                                if (postData.token != keyValueStorage.fcmToken && getPlatformName() == "Android") {
                                    notificationViewModel.sendNotification(
                                        NotificationRequest(
                                            NotificationData(
                                                "Someone Commented On Your Post",
                                                textComment.text
                                            ),
                                            postData.token
                                        )
                                    )
                                }

                                commentCountState = commentCountState.plus(1)
                                keyboardController?.hide()
                                focusManager.clearFocus(true)
                                textComment = TextFieldValue("")
                            }
                    )
                }
            }
        }
    }
}