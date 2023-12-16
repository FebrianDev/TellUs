package ui.screens.post.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.post.state.ListPostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.components.ProgressBarLoading
import ui.screens.post.OptionPostEvent
import ui.screens.post.PostViewModel
import ui.screens.post.items.ItemPost
import ui.screens.post.items.ItemTag
import ui.themes.bgColor
import utils.getUid
import utils.showSnackBar

@Composable
fun LatestPostScreen(
    postViewModel: PostViewModel,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {

    var selectedItem by remember { mutableStateOf(-1) }

    val uid = getUid()

    var uidState by remember { mutableStateOf("") }
    uidState = uid

    LaunchedEffect(false) {
        postViewModel.getAllPost()
    }

    val listTag =
        arrayListOf("Random", "Social", "Study", "Politic", "Technology", "Gaming", "Beauty")

    val listTagState by rememberSaveable { mutableStateOf(listTag) }

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
        postViewModel.listPostState.collectAsState().value.onSuccess {
            when (it) {
                is ListPostState.Loading -> {
                    ProgressBarLoading()
                }

                is ListPostState.Error -> {
                    showSnackBar("Something was wrong!", coroutineScope, scaffoldState)
                }

                is ListPostState.Success -> {
                    val listPost by remember { mutableStateOf(it.data.data?.toMutableStateList()) }
                    val listState = rememberLazyListState()
                    val showButton: Boolean by remember {
                        derivedStateOf { listState.firstVisibleItemIndex > 0 }
                    }
                    Box {
                        LazyColumn(
                            modifier = Modifier.padding(bottom = 64.dp, top = 8.dp),
                            state = listState
                        ) {
                            items(listPost?.toList() ?: listOf()) { data ->
                                event.onDeletePost = { post ->
                                    postViewModel.deletePost(post.id.toString())
                                    listPost?.remove(post)
                                    showSnackBar(
                                        "Success delete post",
                                        coroutineScope,
                                        scaffoldState
                                    )
                                }
                                ItemPost(data, uidState, coroutineScope, scaffoldState, event)
                            }
                        }

                        androidx.compose.animation.AnimatedVisibility(
                            visible = showButton,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically(),
                            modifier = Modifier
                                .padding(bottom = 96.dp)
                        ) {
                            FilledIconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        listState.scrollToItem(index = 0)
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowUp,
                                    contentDescription = "Scroll Top",
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

    @Composable
    fun ScrollToTopButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        FilledIconButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Scroll Top",
            )
        }
    }
}