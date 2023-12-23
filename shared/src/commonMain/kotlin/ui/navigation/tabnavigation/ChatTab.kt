package ui.navigation.tabnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ui.screens.chat.ChatScreen

object ChatTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Chat)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Chat",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        //ChatScreen()
    }

}