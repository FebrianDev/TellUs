package ui.screens.post.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.post.model.PostResponse
import data.post.model.PrivatePostRequest
import data.post.state.PostState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import ui.components.ProgressBarLoading
import ui.components.TextSubtitleMedium
import ui.screens.post.OptionPostEvent
import ui.screens.post.PostViewModel
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.showSnackBar

@Composable
fun MineOptionPost(
    postResponse: PostResponse,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    isPrivate: Boolean,
    event: OptionPostEvent,
) {

    val postViewModel = getViewModel(Unit, viewModelFactory { PostViewModel() })
    var isDialogOpen by remember { mutableStateOf(false) }

    Icon(
        modifier = Modifier
            .width(32.dp)
            .height(32.dp).clickable {
                isDialogOpen = true
            },
        imageVector = Icons.Filled.MoreVert,
        contentDescription = "Options",
        tint = colorPrimary
    )

    if (isDialogOpen) {
        AlertDialog(
            containerColor = bgColor,
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {

            },

            title = {
                TextSubtitleMedium("Options")
            },
            text = {
                Column {
                    TextOption("Copy Text to Clipboard") {
                        isDialogOpen = false
                        event.onCopyText.invoke(postResponse.message.toString())
                    }
                    TextOption("Share Post") {

                    }
                    TextOption("Change to ${if (isPrivate) "Public" else "Private"}") {

                        val privatePostRequest = PrivatePostRequest(!isPrivate)
                        postViewModel.changePrivatePost(
                            privatePostRequest,
                            postResponse.id.toString()
                        )
                        showSnackBar(
                            "Success change to ${if (isPrivate) "Public" else "Private"}",
                            coroutineScope,
                            scaffoldState
                        )
                        event.onChangePrivatePost.invoke(!isPrivate)
                        isDialogOpen = false
                    }
                    TextOption("Delete Post") {
                        event.onDeletePost.invoke(postResponse)
                        isDialogOpen = false
                    }
                }
            },
        )
    }

    postViewModel.postState.collectAsState().value.onSuccess {
        when (it) {
            is PostState.Loading -> {
                ProgressBarLoading()
            }

            is PostState.Error -> {
                showSnackBar(it.message, coroutineScope, scaffoldState)
            }

            is PostState.Success -> {

            }

            else -> {}
        }
    }.onFailure {
        showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
    }

}