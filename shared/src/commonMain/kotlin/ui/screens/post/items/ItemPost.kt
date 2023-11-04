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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.like.model.LikeRequest
import data.post.model.PostResponse
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import ui.screens.post.DetailPostScreen
import ui.screens.post.LikeViewModel
import ui.themes.colorPrimary

@Composable
fun ItemPost(
    postResponse: PostResponse
) {

    val navigator = LocalNavigator.currentOrThrow

    val likeViewModel = getViewModel(Unit, viewModelFactory { LikeViewModel() })
    val likeIcon = remember { mutableStateOf(Icons.Filled.FavoriteBorder) }

    if (postResponse.Likes.isEmpty()) {
        likeIcon.value = Icons.Filled.FavoriteBorder
    } else {
        postResponse.Likes.forEach {
            likeIcon.value = if (it.id_post == postResponse.id && it.id_user == 5) {
                Icons.Filled.Favorite
            } else {
                Icons.Filled.FavoriteBorder
            }
        }

    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "2d\nday",
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
            colors = CardDefaults.cardColors(containerColor = Color.White)

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
                        //LikePost(postResponse, likeViewModel)
                        Icon(
                            modifier = Modifier
                                .padding(top = 4.dp, start = 4.dp)
                                .width(24.dp)
                                .height(24.dp).clickable {
                                    likeViewModel.insertLike(LikeRequest(postResponse.id, 5))
                                    likeIcon.value = if (likeIcon.value == Icons.Filled.Favorite) {
                                        Icons.Filled.FavoriteBorder
                                    } else {
                                        Icons.Filled.Favorite
                                    }

                                },
                            imageVector = likeIcon.value,
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
                        CommentPost(postResponse.comment)
                    }

                    Icon(
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
    }
}

@Composable
fun LikePost(postResponse: PostResponse, likeViewModel: LikeViewModel) {

    var likeIcon = remember { mutableStateOf(Icons.Filled.FavoriteBorder) }

//    when (val likeState = likeViewModel.likeState.collectAsState().value) {
//        is LikeState.Success -> {
//            likeIcon = if (likeState.data.data?.id == 0) {
//                Icons.Filled.FavoriteBorder
//            } else {
//                Icons.Filled.Favorite
//            }
//
//        }
//
//        else -> {}
//    }

    if (postResponse.Likes.isEmpty()) {
        likeIcon.value = Icons.Filled.FavoriteBorder
    } else {
        postResponse.Likes.forEach {
            likeIcon.value = if (it.id_post == postResponse.id && it.id_user == 5) {
                Icons.Filled.Favorite
            } else {
                Icons.Filled.FavoriteBorder
            }
        }

    }

    Icon(
        modifier = Modifier
            .padding(top = 4.dp, start = 4.dp)
            .width(24.dp)
            .height(24.dp).clickable {
                likeViewModel.insertLike(LikeRequest(postResponse.id, 5))
//                likeIcon.value = if (likeIcon.value == Icons.Filled.Favorite) {
//                    Icons.Filled.FavoriteBorder
//                } else {
//                    Icons.Filled.Favorite
//                }

            },
        imageVector = likeIcon.value,
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
}

@Composable
fun CommentPost(comment: Int) {
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

