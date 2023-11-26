package ui.screens.post.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
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
import ui.screens.post.OptionPostEvent
import ui.screens.post.PostViewModel
import ui.screens.post.items.ItemPost
import utils.getUid
import utils.showSnackBar

@Composable
fun BestPostScreen(
    postViewModel: PostViewModel,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope,
) {

    val uid = getUid()

    var uidState by remember { mutableStateOf("") }
    uidState = uid

    if (uidState.isNotEmpty()) postViewModel.getTrending()

    val event = OptionPostEvent()

    Column(modifier = Modifier.fillMaxSize()) {

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

                    LazyColumn(modifier = Modifier.padding(bottom = 64.dp, top = 8.dp)) {
                        items(listPost ?: listOf()) { data ->
                            event.onDeletePost = { post ->
                                postViewModel.deletePost(post.id.toString())
                                listPost?.remove(post)
                                showSnackBar("Success delete post", coroutineScope, scaffoldState)
                            }
                            ItemPost(data, uidState, coroutineScope, scaffoldState, event)
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