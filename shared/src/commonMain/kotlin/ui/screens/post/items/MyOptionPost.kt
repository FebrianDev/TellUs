package ui.screens.post.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import ui.components.TextSubtitleMedium
import ui.themes.colorPrimary

@Composable
fun MyOptionPost() {
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
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {

            },

            title = {
                TextSubtitleMedium("Options")
            },
            text = {
                Column {
                    TextOption("Copy Text to Clipboard")
                    TextOption("Share Post")
                    TextOption("Send Private Message")
                    TextOption("Report Post")
                    TextOption("Block User")
                }
            },
        )
    }}