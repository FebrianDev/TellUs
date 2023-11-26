package ui.screens.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import data.comment.model.CommentReplyRequest
import data.comment.model.CommentResponse
import data.comment.state.InsertCommentState
import data.comment.state.ReplyCommentState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.components.EmptyState
import ui.components.ProgressBarLoading
import ui.components.TextBodyBold
import ui.components.TitleHeader
import ui.screens.post.items.OptionComment
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.getTime
import utils.getUid
import utils.showSnackBar

class DetailCommentScreen(private val commentResponse: CommentResponse) : Screen {

    @Composable
    override fun Content() {

        val uid = getUid()

        val commentViewModel = getViewModel(Unit, viewModelFactory { CommentViewModel() })
        commentViewModel.getReplyComment(
            commentResponse.id_post.toString(),
            commentResponse.id.toString()
        )

        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        var replyMessage by remember { mutableStateOf("") }
        var showReplyMessage by remember { mutableStateOf(false) }
        var isRoot by remember { mutableStateOf(true) }

        Scaffold(
            containerColor = bgColor
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TitleHeader("Detail Comment")
                Spacer(modifier = Modifier.height(24.dp))

                //    DetailPostDummy()
                //ItemComment(commentResponse)
                ShowDetailComment(
                    commentViewModel,
                    scaffoldState,
                    coroutineScope
                ) { message: String, show: Boolean ->
                    replyMessage = message
                    showReplyMessage = show
                    isRoot = true
                }

                Spacer(modifier = Modifier.height(16.dp))

                //ShowComment(commentViewModel, coroutineScope, scaffoldState, replyMessage)

                commentViewModel.replyCommentState.collectAsState().value.onSuccess {
                    when (it) {
                        is ReplyCommentState.Loading -> {
                            ProgressBarLoading()
                        }

                        is ReplyCommentState.Error -> {
                            showSnackBar("Something was wrong!", coroutineScope, scaffoldState)
                        }

                        is ReplyCommentState.Success -> {
                            it.data.data?.let { comment ->
                                if (comment.isEmpty()) {
                                    EmptyState("drawable/ic_no_comment.png", "No Comment")
                                } else {
                                    LazyColumn(modifier = Modifier.fillMaxHeight(if (showReplyMessage) 0.8f else 0.9f)) {
                                        items(comment) { data ->
                                            ItemReplyComment(
                                                data,
                                                scaffoldState,
                                                coroutineScope
                                            ) { message, show ->
                                                replyMessage = message
                                                showReplyMessage = show
                                                isRoot = false
                                            }
                                        }
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

                // ReplyComment(replyMessage)
                if (showReplyMessage) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
                            .border(1.dp, colorPrimary, CircleShape).fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            replyMessage,
                            color = colorPrimary,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(0.9f)
                        )

                        Icon(
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp).clickable {
                                    showReplyMessage = false
                                },
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = colorPrimary
                        )
                    }

                }

                // SendComment(commentViewModel, uid, isRoot)
                //Send Comment
                Row(
                    modifier = Modifier.fillMaxWidth().background(bgColor),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {

                    var textComment by remember { mutableStateOf(TextFieldValue("")) }

                    TextField(
                        value = textComment,
                        onValueChange = {
                            textComment = it
                        },
                        placeholder = { Text(text = "Type your message") },
                        modifier = Modifier.fillMaxWidth(0.8f).background(bgColor),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = bgColor,
                            unfocusedContainerColor = bgColor,
                            disabledContainerColor = bgColor,
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    )

                    TextBodyBold(
                        text = "Send",
                        modifier = Modifier.padding(start = 4.dp, end = 16.dp).wrapContentSize()
                            .align(Alignment.CenterVertically).clickable {
                                commentViewModel.insertReplyComment(
                                    CommentReplyRequest(
                                        id_post = commentResponse.id_post,
                                        id_user = uid,
                                        prev_message = if (isRoot) commentResponse.message else replyMessage,
                                        id_reply = commentResponse.id,
                                        message = textComment.text,
                                        is_root = isRoot,
                                        token = "token"
                                    )
                                )

                                showReplyMessage = false
                                isRoot = true

                                textComment = TextFieldValue("")
                            }
                    )
                }

                commentViewModel.insertCommentState.collectAsState().value.onSuccess {
                    when (it) {
                        is InsertCommentState.Loading -> {
                            ProgressBarLoading()
                        }

                        is InsertCommentState.Error -> {
                            showSnackBar("Something was wrong", coroutineScope, scaffoldState)
                        }

                        is InsertCommentState.Success -> {
                            commentViewModel.getReplyComment(
                                commentResponse.id_post.toString(),
                                commentResponse.id.toString()
                            )
                        }

                        else -> {}
                    }
                }.onFailure {
                    showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
                }


            }

        }
    }

    @Composable
    fun ReplyComment(
        replyMessage: String
    ) {
//        Row(
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                .border(1.dp, colorPrimary, CircleShape).fillMaxWidth()
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                replyMessage,
//                color = colorPrimary,
//                fontSize = 14.sp,
//                modifier = Modifier.fillMaxWidth(0.9f)
//            )
//
//            Icon(
//                modifier = Modifier
//                    .width(24.dp)
//                    .height(24.dp).clickable {
//show
//                    },
//                imageVector = Icons.Filled.Close,
//                contentDescription = "Close",
//                tint = colorPrimary
//            )
//        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun ShowDetailComment(
        commentViewModel: CommentViewModel,
        scaffoldState: SnackbarHostState,
        coroutineScope: CoroutineScope,
        onClick: (message: String, show: Boolean) -> Unit
    ) {
        Row(
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            androidx.compose.material.Text(
                text = getTime(commentResponse.createdAt.toString()),
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterVertically)
            )

            Column {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp, 0.dp, 0.dp, 24.dp),
                    elevation = 4.dp,
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource("drawable/icon_app.png"),
                            contentDescription = ""
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                androidx.compose.material.Text(
                                    commentResponse.message.toString(),
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                )

                                OptionComment(
                                    scaffoldState,
                                    coroutineScope,
                                    commentResponse.message.toString()
                                )
                            }

                            androidx.compose.material.Text(
                                "Reply",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                minLines = 1,
                                modifier = Modifier.clickable {
                                    onClick.invoke(commentResponse.message.toString(), true)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun ShowComment(
        commentViewModel: CommentViewModel,
        coroutineScope: CoroutineScope,
        scaffoldState: SnackbarHostState,
        replyMessage: String
    ) {
//        when (val replyCommentState = commentViewModel.replyCommentState.collectAsState().value) {
//            is ReplyCommentState.Loading -> {
//                ProgressBarLoading()
//            }
//
//            is ReplyCommentState.Error -> {
//                showSnackBar("Something was wrong!", coroutineScope, scaffoldState)
//            }
//
//            is ReplyCommentState.Success -> {
//                replyCommentState.data.data?.let { comment ->
//                    if (comment.isEmpty()) {
//                        EmptyState("drawable/ic_no_comment.png", "No Comment")
//                    } else {
//                        LazyColumn {
////                            items(comment) {
////                                ItemReplyComment(it) { message ->
////                                  // replyMessage = message
////                                }
////                            }
//                        }
//                    }
//                }
//            }
//
//            else -> {}

        // }
    }

    @Composable
    fun SendComment(commentViewModel: CommentViewModel, uid: String, isRoot: Boolean) {
        Row(
            modifier = Modifier.fillMaxWidth().background(bgColor),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            var textComment by remember { mutableStateOf(TextFieldValue("")) }

            TextField(
                value = textComment,
                onValueChange = {
                    textComment = it
                },
                placeholder = { Text(text = "Type your message") },
                modifier = Modifier.fillMaxWidth(0.8f).background(bgColor),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = bgColor,
                    unfocusedContainerColor = bgColor,
                    disabledContainerColor = bgColor,
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            TextBodyBold(
                text = "Send",
                modifier = Modifier.padding(start = 4.dp, end = 16.dp).wrapContentSize()
                    .align(Alignment.CenterVertically).clickable {
                        commentViewModel.insertReplyComment(
                            CommentReplyRequest(
                                id_post = commentResponse.id_post,
                                id_user = uid,
                                prev_message = commentResponse.message,
                                id_reply = commentResponse.id,
                                message = textComment.text,
                                is_root = isRoot,
                                token = "token"
                            )
                        )

                        textComment = TextFieldValue("")
                    }
            )
        }

//        when (val insertCommentState = commentViewModel.insertCommentState.collectAsState().value) {
//            is InsertCommentState.Loading -> {}
//            is InsertCommentState.Error -> {}
//            is InsertCommentState.Success -> {
//                commentViewModel.getReplyComment(
//                    commentResponse.id_post.toString(),
//                    commentResponse.id.toString()
//                )
//            }
//
//            else -> {
//
//            }
//        }
    }
}