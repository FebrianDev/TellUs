package ui.screens.post.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.post.state.ListPostState
import kotlinx.coroutines.CoroutineScope
import ui.components.ProgressBarLoading
import ui.components.rememberDirectionalLazyListState
import ui.screens.post.OptionPostEvent
import ui.screens.post.PostViewModel
import ui.screens.post.items.ItemPost
import utils.ScrollDirection
import utils.getUid
import utils.showSnackBar

@Composable
fun BestPostScreen(
    postViewModel: PostViewModel,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onShowHideBottomBar: (shouldHideBottomBar: ScrollDirection) -> Unit
) {

    val uid = getUid()

    var uidState by remember { mutableStateOf("") }
    uidState = uid

    LaunchedEffect(false) {
        postViewModel.getTrending()
    }

    val event = OptionPostEvent()

    Column(modifier = Modifier.fillMaxSize()) {

        postViewModel.listBestPostState.collectAsState().value.onSuccess {
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
                                showSnackBar("Success delete post", coroutineScope, scaffoldState)
                            }
                            ItemPost(data, uidState, coroutineScope, scaffoldState, event)
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