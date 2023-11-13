package ui.screens.post.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.post.state.ListPostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.components.ProgressBarLoading
import ui.screens.post.InsertPostScreen
import ui.screens.post.PostViewModel
import ui.screens.post.items.ItemMyPost
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.getUid

@Composable
fun MyPostScreen(
    postViewModel: PostViewModel,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {

    val uid = getUid()

    val navigator = LocalNavigator.currentOrThrow

    postViewModel.getPostByIdUser(uid)

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
                                ItemMyPost(data, coroutineScope, scaffoldState)
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

}