package ui.screens.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.comment.model.ReplyCommentResponse
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.components.SpacerH
import ui.components.SpacerW
import ui.screens.comment.option.MyOptionReplyComment
import ui.screens.post.OptionPostEvent
import ui.screens.comment.option.OptionComment
import ui.themes.colorPrimary
import ui.themes.colorThird
import utils.getTime

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ItemReplyComment(
    replyCommentResponse: ReplyCommentResponse,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    uid: String,
    event: OptionPostEvent,
    onClick: (message: String, show: Boolean) -> Unit
) {

    val navigator = LocalNavigator.currentOrThrow

    val commentViewModel = getViewModel(Unit, viewModelFactory { CommentViewModel() })
    commentViewModel.getReplyComment(
        replyCommentResponse.id_post.toString(),
        replyCommentResponse.id.toString()
    )

    val cp = LocalClipboardManager.current

//    event.onCopyText = {
//        cp.setText(AnnotatedString(it))
//        showSnackBar(
//            "The text has been copied successfully",
//            coroutineScope,
//            scaffoldState
//        )
//    }

    Row(
        modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        androidx.compose.material3.Text(
            text = getTime(replyCommentResponse.createdAt.toString()),
            color = Color.Black,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 40.dp, end = 16.dp)
                .align(Alignment.CenterVertically)
        )

        Card(
            modifier = Modifier
                .padding(0.dp, 4.dp, 0.dp, 4.dp)
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier.width(2.dp).heightIn(24.dp).background(colorPrimary)
                        )

                        Text(
                            replyCommentResponse.prev_message.toString(),
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(0.9f).background(
                                colorThird
                            ).padding(2.dp).align(Alignment.CenterVertically)
                        )

                        if (replyCommentResponse.id_user == uid)
                            MyOptionReplyComment(
                                replyCommentResponse, scaffoldState,
                                coroutineScope, event
                            )
                        else
                            OptionComment(
                                scaffoldState,
                                coroutineScope,
                                replyCommentResponse.message.toString(),
                                event
                            )
//                        OptionComment(
//                            scaffoldState,
//                            coroutineScope,
//                            replyCommentResponse.message.toString()
//                        )
                    }

                    Text(
                        replyCommentResponse.message.toString(),
                        color = Color.Black,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth(0.9f).padding(2.dp)
                    )

                    Text(
                        "Reply",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        minLines = 1,
                        modifier = Modifier.clickable {
                            onClick.invoke(
                                replyCommentResponse.message.toString(),
                                true
                            )
                        }
                    )
                }
            }
        }

        SpacerH(4.dp)

    }

}