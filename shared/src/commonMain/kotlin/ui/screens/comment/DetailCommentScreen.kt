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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import data.bookmark.network.NotificationData
import data.bookmark.network.NotificationRequest
import data.comment.model.CommentReplyRequest
import data.comment.model.CommentResponse
import data.comment.state.ReplyCommentState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import getPlatformName
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.components.EmptyState
import ui.components.ProgressBarLoading
import ui.components.SpacerH
import ui.components.SpacerW
import ui.components.TextBodyBold
import ui.components.TextFieldComment
import ui.components.TitleHeader
import ui.screens.NotificationViewModel
import ui.screens.comment.option.MyOptionComment
import ui.screens.comment.option.OptionComment
import ui.screens.post.OptionPostEvent
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.KeyValueStorageImpl
import utils.getIdUser
import utils.getTime
import utils.showSnackBar

@ExperimentalComposeUiApi
class DetailCommentScreen(private val commentResponse: CommentResponse) : Screen {

    @Composable
    override fun Content() {

        val uid = getIdUser()

        val commentViewModel = getViewModel(Unit, viewModelFactory { CommentViewModel() })
        val notificationViewModel = getViewModel(Unit, viewModelFactory { NotificationViewModel() })

        val keyValueStorage = KeyValueStorageImpl()

        commentViewModel.getReplyComment(
            commentResponse.id_post.toString(),
            commentResponse.id.toString()
        )

        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

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

        var replyMessage by remember { mutableStateOf("") }
        var showReplyMessage by remember { mutableStateOf(false) }
        var isRoot by remember { mutableStateOf(true) }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = scaffoldState)
            },
            containerColor = bgColor
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                TitleHeader("Detail Comment")

                SpacerH(24.dp)

                ShowDetailComment(
                    scaffoldState,
                    coroutineScope,
                    uid,
                    event
                ) { message: String, show: Boolean ->
                    replyMessage = message
                    showReplyMessage = show
                    isRoot = true
                }

                SpacerH(16.dp)

                commentViewModel.replyCommentState.collectAsState().value.onSuccess {
                    when (it) {
                        is ReplyCommentState.Loading -> ProgressBarLoading()
                        is ReplyCommentState.Error -> showSnackBar(
                            "Something was wrong!",
                            coroutineScope,
                            scaffoldState
                        )

                        is ReplyCommentState.Success -> {

                            val listCommentState by remember { mutableStateOf(it.data.data?.toMutableStateList()) }

                            if (listCommentState?.isEmpty() == true) {
                                EmptyState("drawable/ic_no_comment.png", "No Comment")
                            } else {
                                LazyColumn(modifier = Modifier.fillMaxHeight(if (showReplyMessage) 0.8f else 0.9f)) {
                                    items(listCommentState ?: listOf()) { data ->
                                        event.onDeleteReplyComment = { commentResponse ->
                                            listCommentState?.remove(commentResponse)
                                            commentViewModel.deleteComment(
                                                commentResponse.id_post.toString(),
                                                commentResponse.id.toString()
                                            )
                                            showSnackBar(
                                                "Success delete comment",
                                                coroutineScope,
                                                scaffoldState
                                            )
                                        }

                                        if (listCommentState?.isEmpty() == true) {
                                            EmptyState("drawable/ic_no_comment.png", "No Comment")
                                        }

                                        ItemReplyComment(
                                            data,
                                            scaffoldState,
                                            coroutineScope,
                                            uid,
                                            event
                                        ) { message, show ->
                                            replyMessage = message
                                            showReplyMessage = show
                                            isRoot = false
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

                                commentViewModel.insertReplyComment(
                                    CommentReplyRequest(
                                        id_post = commentResponse.id_post,
                                        id_user = uid,
                                        prev_message = if (isRoot) commentResponse.message else replyMessage,
                                        id_reply = commentResponse.id,
                                        message = textComment.text,
                                        is_root = isRoot,
                                        token = keyValueStorage.fcmToken
                                    )
                                )

                                if(commentResponse.token != null) {
                                    if (commentResponse.token != keyValueStorage.fcmToken && getPlatformName() == "Android") {
                                        notificationViewModel.sendNotification(
                                            NotificationRequest(
                                                NotificationData(
                                                    "Someone Replied Your Comment",
                                                    textComment.text
                                                ),
                                                commentResponse.token
                                            )
                                        )
                                    }
                                }

                                showReplyMessage = false
                                isRoot = true

                                keyboardController?.hide()
                                focusManager.clearFocus(true)

                                textComment = TextFieldValue("")
                            }
                    )
                }
            }

        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun ShowDetailComment(
        scaffoldState: SnackbarHostState,
        coroutineScope: CoroutineScope,
        uid: String,
        event: OptionPostEvent,
        onClick: (message: String, show: Boolean) -> Unit
    ) {
        Row(
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            androidx.compose.material3.Text(
                text = getTime(commentResponse.createdAt),
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

                        SpacerW(4.dp)

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

                                if (uid == commentResponse.id_user) {
                                    MyOptionComment(
                                        commentResponse, scaffoldState, coroutineScope, event
                                    )
                                } else {
                                    OptionComment(
                                        scaffoldState,
                                        coroutineScope,
                                        commentResponse.message,
                                        event
                                    )
                                }
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

}