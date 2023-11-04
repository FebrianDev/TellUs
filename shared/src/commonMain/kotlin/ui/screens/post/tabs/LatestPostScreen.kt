package ui.screens.post.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.post.state.ListPostState
import ui.screens.post.PostViewModel
import ui.screens.post.items.ItemPost
import ui.screens.post.items.ItemTag

@Composable
fun LatestPostScreen(
    postViewModel: PostViewModel
) {

    //  val postViewModel = getViewModel(Unit, viewModelFactory { PostViewModel() })

    LaunchedEffect(true) {
        postViewModel.getAllPost()
    }

    val listTag =
        arrayListOf("Random", "Social", "Study", "Politic", "Technology", "Gaming", "Beauty")

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        LazyRow(modifier = Modifier.padding(start = 6.dp, top = 4.dp, end = 6.dp, bottom = 4.dp)) {
            items(listTag) {
                ItemTag(it) {
                    postViewModel.getPostByTag("Social")
                }
            }
        }

        when (val postState = postViewModel.listPostState.collectAsState().value) {
            is ListPostState.Loading -> {
                CircularProgressIndicator()
            }

            is ListPostState.Error -> {

            }

            is ListPostState.Success -> {
                LazyColumn(modifier = Modifier.padding(bottom = 64.dp, top = 8.dp)) {
                    items(postState.data.data ?: listOf()) {
                        ItemPost(it)
                    }
                }
            }

            else -> {}
        }

    }
}