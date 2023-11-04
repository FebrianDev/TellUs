package ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.components.TopBar

@Composable
fun ChatScreen() {
    Column(
        modifier = Modifier.fillMaxWidth().wrapContentSize()
    ) {
        TopBar("Chat")
    }
}