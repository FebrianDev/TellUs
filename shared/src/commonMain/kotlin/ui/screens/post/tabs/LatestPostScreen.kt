package ui.screens.post.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.post.state.ListPostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.components.ProgressBarLoading
import ui.screens.post.PostViewModel
import ui.screens.post.items.ItemPost
import ui.screens.post.items.ItemTag
import ui.themes.bgColor

@Composable
fun LatestPostScreen(
    postViewModel: PostViewModel,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {

    var selectedItem by remember { mutableStateOf(-1) }

    LaunchedEffect(false) {
        postViewModel.getAllPost()
    }

    val listTag =
        arrayListOf("Random", "Social", "Study", "Politic", "Technology", "Gaming", "Beauty")
    val listTagState by rememberSaveable { mutableStateOf(listTag) }

    Column(modifier = Modifier.fillMaxSize().background(bgColor)) {
        LazyRow(modifier = Modifier.padding(start = 6.dp, top = 4.dp, end = 6.dp, bottom = 4.dp)) {
            items(listTagState.size) {
                ItemTag(listTag[it], isHighlight = selectedItem == it) {
                    if (selectedItem == it) {
                        selectedItem = -1
                        postViewModel.getAllPost()
                    } else {
                        selectedItem = it
                        postViewModel.getPostByTag(listTag[it])
                    }
                }
            }
        }

        //handle network
        postViewModel.listPostState.collectAsState().value.onSuccess {
            when (it) {
                is ListPostState.Loading -> {
                    ProgressBarLoading()
                }

                is ListPostState.Error -> {
                    coroutineScope.launch {
                        scaffoldState.showSnackbar(
                            message = "Something was wrong!",
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is ListPostState.Success -> {
                    LazyColumn(modifier = Modifier.padding(bottom = 64.dp, top = 8.dp)) {
                        items(it.data.data ?: listOf()) { data ->
                            ItemPost(data, coroutineScope, scaffoldState)
                        }
                    }
                }

                else -> {}
            }
        }.onFailure {
            coroutineScope.launch {
                scaffoldState.showSnackbar(
                    message = it.message.toString(),
                    duration = SnackbarDuration.Short
                )
            }
        }

    }
}