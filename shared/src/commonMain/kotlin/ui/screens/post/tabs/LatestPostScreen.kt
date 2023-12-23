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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import data.post.state.ListPostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.components.ProgressBarLoading
import ui.components.rememberDirectionalLazyListState
import ui.screens.post.OptionPostEvent
import ui.screens.post.PostViewModel
import ui.screens.post.items.ItemPost
import ui.screens.post.items.ItemPost2
import ui.screens.post.items.ItemTag
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.ScrollDirection
import utils.getUid
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

    var uidState by remember { mutableStateOf("") }
    uidState = uid

    LaunchedEffect(uidState) {
        if(uidState.isNotEmpty()) postViewModel.getAllPost()
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
        postViewModel.listPostState.collectAsState().value.onSuccess { it ->
            when (it) {
                is ListPostState.Loading -> {
                    ProgressBarLoading()
                }

                is ListPostState.Error -> {
                    showSnackBar("Something was wrong!", coroutineScope, scaffoldState)
                }

                is ListPostState.Success -> {
                    val listPost by remember { mutableStateOf(it.data.data?.toMutableStateList()) }

                    val listState = rememberForeverLazyListState(key = "latest")
                    val showButton: Boolean by remember {
                        derivedStateOf { listState.firstVisibleItemIndex > 0 }
                    }

                    val directionalLazyListState = rememberDirectionalLazyListState(
                        listState
                    )

                    var likeIcon by remember { mutableStateOf(Icons.Filled.FavoriteBorder) }
                    var bookmarkIcon by remember { mutableStateOf(Icons.Filled.BookmarkBorder) }


                    Box {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp),
                            state = listState
                        ) {
                            items(listPost?.toList() ?: listOf()) { postResponse ->

                                var stateLike by rememberSaveable {
                                    mutableStateOf(if (postResponse.Likes.isEmpty()) {
                                        true
                                    } else {
                                        val likedPosts = postResponse.Likes.filter {
                                            it.id_post == postResponse.id && it.id_user == uid
                                        }

                                        likedPosts.isEmpty()
                                    })
                                }

                                event.onDeletePost = { post ->
                                    postViewModel.deletePost(post.id.toString())
                                    listPost?.remove(post)
                                    showSnackBar(
                                        "Success delete post",
                                        coroutineScope,
                                        scaffoldState
                                    )
                                }

                                event.onLikePost = {
                                    if (postResponse.Likes.isEmpty()) {
                                        Icons.Filled.Favorite
                                        stateLike = true
                                    } else {
                                        val likedPosts = postResponse.Likes.filter {
                                            it.id_post == postResponse.id && it.id_user == uid
                                        }

                                        if (likedPosts.isNotEmpty()) {
                                            Icons.Filled.FavoriteBorder
                                            stateLike = false
                                        } else {
                                            stateLike = true
                                            Icons.Filled.Favorite
                                        }
                                    }
                                    // stateLike = it
                                }

//                                if (postResponse.Likes.isEmpty()) {
//                                    stateLike = false
//                                    Icons.Filled.FavoriteBorder
//                                } else {
//                                    val likedPosts = postResponse.Likes.filter {
//                                        it.id_post == postResponse.id && it.id_user == uid
//                                    }
//
//                                    if (likedPosts.isNotEmpty()) {
//                                        stateLike = true
//                                        Icons.Filled.Favorite
//                                    } else {
//                                        stateLike = false
//                                        Icons.Filled.FavoriteBorder
//                                    }
//                                }
//
//                                if (postResponse.Bookmarks.isEmpty()) {
//                                    bookmarkIcon = Icons.Filled.BookmarkBorder
//                                } else {
//                                    postResponse.Bookmarks.forEach {
//                                        bookmarkIcon =
//                                            if (it.id_post == postResponse.id && it.id_user == uid) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder
//                                    }
//                                }

                                if (postResponse.Bookmarks.isEmpty()) {
                                    bookmarkIcon = Icons.Filled.BookmarkBorder
                                } else {
                                    postResponse.Bookmarks.forEach {
                                        bookmarkIcon =
                                            if (it.id_post == postResponse.id && it.id_user == uid) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder
                                    }
                                }

//                                ItemPost2(
//                                    postResponse,
//                                    uidState,
//                                    coroutineScope,
//                                    scaffoldState,
//                                    stateLike,
//                                    bookmarkIcon,
//                                    event
//                                )
                               // ItemPost(data, uidState, coroutineScope, scaffoldState, listState,event)
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

@Composable
fun rememberForeverLazyListState(
    key: String,
    params: String = "",
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0
): LazyListState {
    val scrollState = rememberSaveable(saver = LazyListState.Saver) {
        var savedValue = SaveMap[key]
        if (savedValue?.params != params) savedValue = null
        val savedIndex = savedValue?.index ?: initialFirstVisibleItemIndex
        val savedOffset = savedValue?.scrollOffset ?: initialFirstVisibleItemScrollOffset
        LazyListState(
            savedIndex,
            savedOffset
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            val lastIndex = scrollState.firstVisibleItemIndex
            val lastOffset = scrollState.firstVisibleItemScrollOffset
            SaveMap[key] = KeyParams(params, lastIndex, lastOffset)
        }
    }
    return scrollState
}

private val SaveMap = mutableMapOf<String, KeyParams>()

private data class KeyParams(
    val params: String = "",
    val index: Int,
    val scrollOffset: Int
)



