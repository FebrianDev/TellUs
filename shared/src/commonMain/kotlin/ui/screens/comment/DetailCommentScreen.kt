package ui.screens.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import data.comment.state.CommentState
import data.comment.state.InsertCommentState
import data.comment.model.CommentRequest
import data.comment.model.CommentResponse
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.components.EmptyState
import ui.components.TextBodyBold
import ui.components.TitleHeader
import ui.screens.post.items.OptionPost
import ui.themes.colorPrimary

class DetailCommentScreen(private val commentResponse: CommentResponse) : Screen {

    @Composable
    override fun Content() {

        val commentViewModel = getViewModel(Unit, viewModelFactory { CommentViewModel() })
        commentViewModel.getReplyComment(
            commentResponse.id_post.toString(),
            commentResponse.id.toString()
        )

        Scaffold {
            Column(modifier = Modifier.fillMaxSize()) {
                TitleHeader("Detail Comment")
                Spacer(modifier = Modifier.height(24.dp))

                //    DetailPostDummy()
                ShowDetailComment(commentViewModel)

                Spacer(modifier = Modifier.height(16.dp))

                ShowComment(commentViewModel)

                Spacer(modifier = Modifier.weight(1f))

                SendComment(commentViewModel)
            }

        }
    }

    @Composable
    fun ShowDetailComment(commentViewModel: CommentViewModel) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Card(
                modifier = Modifier
                    .padding(0.dp, 4.dp, 16.dp, 4.dp)
                    .fillMaxWidth()
                    .heightIn(min = 96.dp, max = 256.dp)
                    .clickable {
                        // navigator.push(DetailPostTab)
                    },
                shape = RoundedCornerShape(0.dp, 24.dp, 24.dp, 0.dp),
                elevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
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

                        OptionPost()
                    }

                    androidx.compose.material.Text(
                        "Reply",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                    )
                }
            }

            androidx.compose.material.Text(
                text = "2d\nday",
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterVertically)
            )
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable()
    fun ShowComment(commentViewModel: CommentViewModel) {
        when (val commentState = commentViewModel.commentState.collectAsState().value) {
            is CommentState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorPrimary
                    )
                }
            }

            is CommentState.Error -> {

            }

            is CommentState.Success -> {
                commentState.data.data?.let { comment ->
                    if (comment.isEmpty()) {
                        EmptyState("drawable/ic_no_comment.png", "No Comment")

                    } else {
                        LazyColumn {

                            items(comment) {
                                ItemComment(it)
                            }
                        }
                    }
                }
            }

            else -> {}

        }
    }

    @Composable
    fun SendComment(commentViewModel: CommentViewModel) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            var textComment by remember { mutableStateOf(TextFieldValue("")) }

            TextField(
                value = textComment,
                onValueChange = {
                    textComment = it
                },
                placeholder = { Text(text = "Type your message") },
                modifier = Modifier.fillMaxWidth(0.8f).background(Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                ),
            )

            TextBodyBold(
                text = "Send",
                modifier = Modifier.padding(start = 4.dp, end = 16.dp).wrapContentSize()
                    .align(Alignment.CenterVertically).clickable {
                        commentViewModel.insertComment(
                            CommentRequest(
                                id_post = commentResponse.id_post,
                                id_user = 5,
                                id_reply = commentResponse.id,
                                message = textComment.text,
                                root = true,
                                token = "token"
                            )
                        )

                        textComment = TextFieldValue("")
                    }
            )
        }

        when (val insertCommentState = commentViewModel.insertCommentState.collectAsState().value) {
            is InsertCommentState.Loading -> {}
            is InsertCommentState.Error -> {}
            is InsertCommentState.Success -> {
                commentViewModel.getReplyComment(
                    commentResponse.id_post.toString(),
                    commentResponse.id.toString()
                )
            }

            else -> {

            }
        }
    }
}