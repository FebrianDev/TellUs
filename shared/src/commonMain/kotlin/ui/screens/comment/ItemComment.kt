package ui.screens.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Text
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.comment.model.CommentResponse
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.themes.colorPrimary

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ItemComment(
    commentResponse: CommentResponse,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {

    val navigator = LocalNavigator.currentOrThrow

    val commentViewModel = getViewModel(Unit, viewModelFactory { CommentViewModel() })
    commentViewModel.getReplyComment(
        commentResponse.id_post.toString(),
        commentResponse.id.toString()
    )

    Row(
        modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "2d\nday",
            color = Color.Black,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                .align(Alignment.CenterVertically)
        )

        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigator.push(DetailCommentScreen(commentResponse))
                    },
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

                            Text(
                                commentResponse.message.toString(),
                                color = Color.Black,
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth(0.9f)
                            )

                            OptionComment(scaffoldState, coroutineScope)
                        }

                        Text(
                            "Reply",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            minLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))


            if (commentResponse.ReplyComments?.isEmpty() == false) {
                androidx.compose.material3.Text(
                    "View Reply(${commentResponse.ReplyComments.size})",
                    modifier = Modifier.padding(bottom = 4.dp).align(Alignment.CenterHorizontally),
                    color = colorPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }

        }
    }
}

@Composable
fun ItemCommentDummy() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "2d\nday",
            color = Color.Black,
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
                    // navigator.push(DetailPostTab)
                },
            shape = RoundedCornerShape(24.dp, 0.dp, 0.dp, 24.dp),
            elevation = 4.dp,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        "Lörem ipsum ohet tynett nysare kårade. ",
                        color = Color.Black,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )

                    //OptionComment(s)
                }

                Text(
                    "Reply",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                )
            }
        }
    }
}