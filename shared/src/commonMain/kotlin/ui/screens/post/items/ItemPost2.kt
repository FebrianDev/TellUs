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
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.bookmark.model.BookmarkRequest
import data.chat.ChatEntity
import data.like.model.LikeRequest
import data.post.model.PostResponse
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import ui.screens.bookmark.BookmarkViewModel
import ui.screens.chat.ChatViewModel
import ui.screens.post.DetailPostScreen
import ui.screens.post.LikeViewModel
import ui.screens.post.OptionPostEvent
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.generatedFakeName
import utils.getDateNow
import utils.getTime
import utils.showSnackBar

@Composable
fun ItemPost2(
    postResponse: PostResponse,
    uid: String,
    coroutineScope: CoroutineScope,
    scaffoldState: SnackbarHostState,
    iconLike : Boolean,
    iconBookmark : ImageVector,
    event: OptionPostEvent
) {

    val navigator = LocalNavigator.currentOrThrow

    val chatViewModel = getViewModel(Unit, viewModelFactory { ChatViewModel() })

    val cp = LocalClipboardManager.current

    event.onCopyText = {
        cp.setText(AnnotatedString(it))
        showSnackBar(
            "The text has been copied successfully",
            coroutineScope,
            scaffoldState
        )
    }

    event.onSendPrivateChat = {
        chatViewModel.createRoom(
            ChatEntity(
                id_chat = "",
                id_sent = "idUser",
                id_receiver = postResponse.id_user,
                id_post = postResponse.id.toString(),
                post_message = postResponse.message,
                name = generatedFakeName(),
                message = ArrayList(),
                countReadSent = 0,
                countReadReceiver = 0,
                date = getDateNow(),
                token = "token"
            )
        )
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = getTime(postResponse.createdAt),
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
                    navigator.push(DetailPostScreen(postResponse.id))
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
                    Text(
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
                        Icon(
                            modifier = Modifier
                                .padding(top = 4.dp, start = 4.dp)
                                .width(24.dp)
                                .height(24.dp).clickable {
                                    event.onLikePost.invoke(!iconLike)
                                },
                            imageVector = if (iconLike) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Btn Like",
                            tint = colorPrimary
                        )

                        // Text Like Count
                        Text(
                            text = postResponse.like.toString(),
                            color = colorPrimary,
                            modifier = Modifier
                                .padding(top = 4.dp, start = 6.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.width(4.dp))
                        CommentPost2(postResponse.comment)
                    }

                    Icon(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 4.dp)
                            .width(24.dp)
                            .height(24.dp).clickable {
                               event.onBookmarkPost.invoke()
                            },

                        imageVector = iconBookmark,
                        contentDescription = "Btn Bookmark",
                        tint = colorPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun CommentPost2(comment: Int) {
    Icon(
        modifier = Modifier
            .padding(top = 4.dp, start = 4.dp)
            .width(24.dp)
            .height(24.dp),

        imageVector = Icons.Filled.Comment,
        contentDescription = "Btn Comment",
        tint = colorPrimary
    )

    // Text Like Count
    Text(
        text = comment.toString(),
        color = colorPrimary,
        modifier = Modifier
            .padding(top = 4.dp, start = 6.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )

}

