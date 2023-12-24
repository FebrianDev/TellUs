package ui.screens.chat

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
import ui.components.TextSubtitleMedium
import ui.screens.post.OptionPostEvent
import ui.screens.post.items.TextOption
import ui.themes.bgColor
import ui.themes.colorPrimary

@Composable
fun OptionChat(
    postResponse: PostResponse,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    event: OptionPostEvent,
) {
    var isDialogOpen by remember { mutableStateOf(false) }

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
                    TextOption("Leave Comment") {
                        isDialogOpen = false
                        event.onCopyText.invoke(postResponse.message.toString())
                    }
                    TextOption("Send Private Message"){

                    }
                }
            },
        )
    }
}