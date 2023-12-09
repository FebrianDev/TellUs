package ui.screens.comment.option

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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import data.comment.model.CommentResponse
import data.comment.model.ReplyCommentResponse
import kotlinx.coroutines.CoroutineScope
import ui.components.AlertDialogComposable
import ui.components.TextSubtitleMedium
import ui.screens.post.OptionPostEvent
import ui.screens.post.items.TextOption
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.showSnackBar

@Composable
fun MyOptionReplyComment(
    commentResponse: ReplyCommentResponse,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    event: OptionPostEvent
) {

    var isDialogOpen by remember { mutableStateOf(false) }
    var isDialogDelete by remember { mutableStateOf(false) }

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

    val cp = LocalClipboardManager.current

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
                        cp.setText(AnnotatedString(commentResponse.message.toString()))
                        showSnackBar(
                            "The text has been copied successfully",
                            coroutineScope,
                            scaffoldState
                        )
                        event.onCopyText.invoke(commentResponse.message.toString())
                    }

                    TextOption("Delete Comment") {
                        isDialogOpen = false
                        isDialogDelete = true
                    }
                }
            },
        )
    }

    if (isDialogDelete) {
        AlertDialogComposable(
            onDismissRequest = { isDialogDelete = false },
            onConfirmation = {
                event.onDeleteReplyComment.invoke(commentResponse)
                isDialogDelete = false
            },
            "Delete Post",
            "Are you sure delete this post?"
        )
    }
}