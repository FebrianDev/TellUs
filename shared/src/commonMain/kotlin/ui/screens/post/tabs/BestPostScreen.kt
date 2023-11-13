package ui.screens.post.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.post.state.ListPostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.components.ProgressBarLoading
import ui.screens.post.PostViewModel
import ui.screens.post.items.ItemPost

@Composable
fun BestPostScreen(
    postViewModel: PostViewModel,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {

    //   LaunchedEffect(true){
    postViewModel.getTrending()
    //   }

    Column(modifier = Modifier.fillMaxSize()) {

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
                    message = "Internal server error",
                    duration = SnackbarDuration.Short
                )
            }
        }

    }
}