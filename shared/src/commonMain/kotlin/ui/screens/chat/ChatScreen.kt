package ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ui.components.TopBar
import ui.themes.bgColor

@Composable
fun ChatScreen() {

    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        containerColor = bgColor
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentSize()
        ) {
            TopBar("Chat")
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxHeight(0.9f)) {
                items(5) { item ->
                    ItemChat() {
                        navigator.push(ChatRoomScreen())
                    }
                }
            }
        }
    }
}

