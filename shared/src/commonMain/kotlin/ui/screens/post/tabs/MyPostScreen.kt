package ui.screens.post.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.post.state.ListPostState
import kotlinx.coroutines.CoroutineScope
import ui.components.ProgressBarLoading
import ui.components.rememberDirectionalLazyListState
import ui.screens.post.InsertPostScreen
import ui.screens.post.OptionPostEvent
import ui.screens.post.PostViewModel
import ui.screens.post.items.ItemMyPost
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.ScrollDirection
import utils.getUid
import utils.showSnackBar

@Composable
fun MyPostScreen(
    postViewModel: PostViewModel,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onShowHideBottomBar: (shouldHideBottomBar: ScrollDirection) -> Unit
) {

    val navigator = LocalNavigator.currentOrThrow

    val uid = getUid()

    LaunchedEffect(uid) {
        postViewModel.getPostByIdUser(uid)
    }

    val event = OptionPostEvent()

    Scaffold(
        containerColor = bgColor,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = bgColor,
                shape = CircleShape,
                onClick = {
                    navigator.push(InsertPostScreen())
                },
                modifier = Modifier.padding(end = 8.dp, bottom = 56.dp)
            ) {
                Icon(Icons.Filled.Add, "Floating action button.", tint = colorPrimary)
            }
        }
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            postViewModel.listMyPostState.collectAsState().value.onSuccess {
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
                        val directionalLazyListState = rememberDirectionalLazyListState(
                            listState
                        )
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp),
                            state = listState
                        ) {
                            items(listPost ?: listOf()) { data ->
                                event.onDeletePost = { post ->
                                    postViewModel.deletePost(post.id.toString())
                                    listPost?.remove(post)
                                    showSnackBar(
                                        "Success delete post",
                                        coroutineScope,
                                        scaffoldState
                                    )
                                }

                                ItemMyPost(data, coroutineScope, scaffoldState, uid, event)
                            }
                        }

                        onShowHideBottomBar.invoke(directionalLazyListState.scrollDirection)
                    }

                    else -> {}
                }
            }.onFailure {
                showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
            }
        }
    }

}