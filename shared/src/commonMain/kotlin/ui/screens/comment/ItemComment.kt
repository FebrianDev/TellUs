package ui.screens.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
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
import ui.screens.post.items.OptionPost

@Composable
fun ItemComment(
    commentResponse: CommentResponse
) {

    val navigator = LocalNavigator.currentOrThrow

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
                    navigator.push(DetailCommentScreen(commentResponse))
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
                        commentResponse.message.toString(),
                        color = Color.Black,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )

                    OptionPost()
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

                    OptionPost()
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