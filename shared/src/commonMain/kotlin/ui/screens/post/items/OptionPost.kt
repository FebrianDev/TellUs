package ui.screens.post.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.post.model.PostResponse
import kotlinx.coroutines.CoroutineScope
import ui.components.AlertDialogComposable
import ui.components.TextSubtitleMedium
import ui.screens.post.OptionPostEvent
import ui.themes.bgColor
import ui.themes.colorPrimary

@Composable
fun OptionPost(
    postResponse: PostResponse,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    event: OptionPostEvent,
) {

    var isDialogOpen by remember { mutableStateOf(false) }
    var isDialogPrivateChat by remember { mutableStateOf(false) }

    Icon(
        modifier = Modifier
            .padding(start = 6.dp)
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
                        event.onCopyText.invoke(postResponse.message)
                    }

                    TextOption("Send Private Message") {
                        isDialogOpen = false
                        isDialogPrivateChat = true
                    }
                }
            },
        )
    }

    if (isDialogPrivateChat) {
        AlertDialogComposable(
            onDismissRequest = { isDialogPrivateChat = false },
            onConfirmation = {
                event.onSendPrivateChat.invoke()
                isDialogPrivateChat = false
            },
            "Send Private Chat",
            "Are you sure send private chat?"
        )
    }
}