package ui.screens.post.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.post.state.ListPostState
import ui.screens.post.PostViewModel
import ui.screens.post.items.ItemPost

@Composable
fun BestPostScreen(
    postViewModel: PostViewModel
) {

    //   LaunchedEffect(true){
    postViewModel.getTrending()
    //   }

    Column(modifier = Modifier.fillMaxSize()) {
        when (val postState = postViewModel.listPostState.collectAsState().value) {
            is ListPostState.Loading -> {}
            is ListPostState.Error -> {}
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