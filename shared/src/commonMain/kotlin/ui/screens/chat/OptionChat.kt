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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.components.AlertDialogComposable
import ui.components.TextSubtitleMedium
import ui.screens.post.items.TextOption
import ui.themes.bgColor

@Composable
fun OptionChat(
    onLeaveChat: () -> Unit = {}
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
        tint = Color.White
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
                    TextOption("Leave Chat") {
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
                onLeaveChat.invoke()
                isDialogOpen = false
            },
            "Leave Chat",
            "Are you sure leave chat?"
        )
    }
}