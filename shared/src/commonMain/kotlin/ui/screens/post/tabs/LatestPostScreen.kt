package ui.screens.post.tabs

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.post.model.PostResponse
import data.post.state.ListPostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.components.ProgressBarLoading
import ui.components.rememberDirectionalLazyListState
import ui.screens.post.OptionPostEvent
import ui.screens.post.PostViewModel
import ui.screens.post.items.ItemPost
import ui.screens.post.items.ItemTag
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.ScrollDirection
import utils.getUid
import utils.rememberForeverLazyListState
import utils.showSnackBar

@Composable
fun LatestPostScreen(
    postViewModel: PostViewModel,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onShowHideBottomBar: (shouldHideBottomBar: ScrollDirection) -> Unit
) {

    var selectedItem by remember { mutableStateOf(-1) }

    val uid = getUid()

    LaunchedEffect(false) {
        postViewModel.getAllPost()
    }

    val listTag =
        arrayListOf("Random", "Social", "Study", "Politic", "Technology", "Gaming", "Beauty")

    val listTagState by rememberSaveable { mutableStateOf(listTag) }
    var listPost by remember { mutableStateOf(listOf<PostResponse>().toMutableStateList()) }

    val event = OptionPostEvent()

    Column(modifier = Modifier.fillMaxSize().background(bgColor)) {
        LazyRow(modifier = Modifier.padding(start = 6.dp, top = 4.dp, end = 6.dp, bottom = 4.dp)) {
            items(listTagState.size) {
                ItemTag(listTag[it], isHighlight = selectedItem == it) {
                    if (selectedItem == it) {
                        selectedItem = -1
                        postViewModel.getAllPost()
                    } else {
                        selectedItem = it
                        postViewModel.getAllPost(listTag[selectedItem])
                    }
                }
            }
        }

        //handle network
        postViewModel.listPostState.collectAsState().value.onSuccess { it ->
            when (it) {
                is ListPostState.Loading -> {
                    ProgressBarLoading()
                }

                is ListPostState.Error -> {
                    showSnackBar("Something was wrong!", coroutineScope, scaffoldState)
                }

                is ListPostState.Success -> {
                    listPost = it.data.data?.toMutableStateList()!!
                    val listState = rememberForeverLazyListState(key = "latest")
                    val showButton: Boolean by remember {
                        derivedStateOf { listState.firstVisibleItemIndex > 0 }
                    }

                    val directionalLazyListState = rememberDirectionalLazyListState(
                        listState
                    )

                    Box {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp),
                            state = listState
                        ) {
                            items(listPost.toList()) { postResponse ->

                                event.onDeletePost = { post ->
                                    postViewModel.deletePost(post.id.toString())
                                    listPost.remove(post)
                                    showSnackBar(
                                        "Success delete post",
                                        coroutineScope,
                                        scaffoldState
                                    )
                                }

                                ItemPost(
                                    postResponse,
                                    uid,
                                    coroutineScope,
                                    scaffoldState,
                                    event
                                )
                            }
                        }

                        onShowHideBottomBar.invoke(directionalLazyListState.scrollDirection)

                        androidx.compose.animation.AnimatedVisibility(
                            visible = showButton,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically(),
                            modifier = Modifier
                                .align(Alignment.BottomStart).padding(bottom = 20.dp, start = 8.dp)
                        ) {
                            FilledIconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        listState.scrollToItem(index = 0)
                                    }
                                },
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = colorPrimary
                                ),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowUp,
                                    contentDescription = "Scroll Top",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                }

                else -> {}
            }
        }.onFailure {
            showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
        }

    }
}
