@file:OptIn(ExperimentalMaterial3Api::class)

package ui.screens.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import data.comment.state.CommentState
import data.comment.state.InsertCommentState
import data.comment.model.CommentRequest
import data.post.model.PostResponse
import data.post.state.PostState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import ui.components.EmptyState
import ui.components.TextBodyBold
import ui.components.TitleHeader
import ui.screens.comment.CommentViewModel
import ui.screens.comment.ItemComment
import ui.screens.post.items.OptionPost
import ui.themes.colorPrimary

@OptIn(ExperimentalMaterial3Api::class)
class DetailPostScreen(private val id: Int) : Screen {

    @Composable
    override fun Content() {

        val postViewModel = getViewModel(Unit, viewModelFactory { PostViewModel() })
        postViewModel.getPostById(id.toString())

        val commentViewModel = getViewModel(Unit, viewModelFactory { CommentViewModel() })
        commentViewModel.getCommentById(id.toString())

        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = scaffoldState)
            },
            containerColor = Color.White,
            contentColor = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TitleHeader("Detail Post")
                Spacer(modifier = Modifier.height(24.dp))

                //    DetailPostDummy()
                ShowDetailPost(postViewModel)

                Spacer(modifier = Modifier.height(16.dp))

                ShowComment(commentViewModel)

//                LazyColumn {
//                    items(5) {
//                        ItemComment()
//                    }
//                }


                Spacer(modifier = Modifier.weight(1f))

                SendComment(commentViewModel)
            }
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
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            TextBodyBold(
                text = "Send",
                modifier = Modifier.padding(start = 4.dp, end = 16.dp).wrapContentSize()
                    .align(Alignment.CenterVertically).clickable {
                        commentViewModel.insertComment(
                            CommentRequest(
                                id_post = id,
                                id_user = 5,
                                id_reply = 0,
                                message = textComment.text,
                                root = false,
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
                commentViewModel.getCommentById(id.toString())
            }

            else -> {

            }
        }
    }

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
                        LazyColumn(modifier = Modifier.fillMaxHeight(0.9f)) {

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
    fun ShowDetailPost(postViewModel: PostViewModel) {
        when (val postState = postViewModel.postState.collectAsState().value) {
            is PostState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorPrimary
                    )
                }
            }

            is PostState.Error -> {

            }

            is PostState.Success -> {
                postState.data.data?.let { DetailPost(it) }
            }

            else -> {}

        }
    }

    @Composable
    fun DetailPost(postResponse: PostResponse) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Card(
                modifier = Modifier
                    .padding(0.dp, 4.dp, 16.dp, 4.dp)
                    .fillMaxWidth()
                    .heightIn(min = 96.dp, max = 256.dp),
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

                        OptionPost()
                    }

                    Divider(
                        color = colorPrimary,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            LikePost(postResponse.like)
                            Spacer(Modifier.width(4.dp))
                            CommentPost(postResponse.comment)
                        }

                        androidx.compose.material.Icon(
                            modifier = Modifier
                                .padding(top = 4.dp, start = 4.dp)
                                .width(24.dp)
                                .height(24.dp),

                            imageVector = Icons.Filled.Bookmark,
                            contentDescription = "Btn Bookmark",
                            tint = colorPrimary
                        )
                    }
                }
            }

            androidx.compose.material.Text(
                text = "2d\nday",
                color = colorPrimary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterVertically)
            )
        }

    }

    @Composable
    fun DetailPostDummy() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Card(
                modifier = Modifier
                    .padding(0.dp, 4.dp, 16.dp, 4.dp)
                    .fillMaxWidth()
                    .heightIn(min = 96.dp, max = 256.dp),
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
                            "ostResponse.message",
                            color = colorPrimary,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(0.9f)
                        )

                        OptionPost()
                    }

                    Divider(
                        color = colorPrimary,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            LikePost(1)
                            Spacer(Modifier.width(4.dp))
                            CommentPost(1)
                        }

                        androidx.compose.material.Icon(
                            modifier = Modifier
                                .padding(top = 4.dp, start = 4.dp)
                                .width(24.dp)
                                .height(24.dp),

                            imageVector = Icons.Filled.Bookmark,
                            contentDescription = "Btn Bookmark",
                            tint = colorPrimary
                        )
                    }
                }
            }

            androidx.compose.material.Text(
                text = "2d\nday",
                color = colorPrimary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterVertically)
            )
        }

    }


    @Composable
    fun LikePost(like: Int) {
        androidx.compose.material.Icon(
            modifier = Modifier
                .padding(top = 4.dp, start = 4.dp)
                .width(24.dp)
                .height(24.dp),

            imageVector = Icons.Filled.Favorite,
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

    /* @Composable
     fun TitleHeader() {

         val navigator = LocalNavigator.currentOrThrow

         Row(modifier = Modifier.padding(start = 12.dp, top = 16.dp)) {
             Icon(
                 imageVector = Icons.Default.ArrowBackIosNew,
                 contentDescription = "",
                 modifier = Modifier.clickable {
                     navigator.pop()
                 }
             )

             Spacer(Modifier.width(4.dp))

             Text(
                 text = "Detail Post",
                 fontSize = 20.sp,
                 fontWeight = FontWeight.ExtraBold,
                 color = colorPrimary,
             )
         }
     }*/
}


