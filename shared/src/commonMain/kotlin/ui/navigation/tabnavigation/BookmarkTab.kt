package ui.navigation.tabnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ui.screens.bookmark.BookmarkScreen

object BookmarkTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Bookmarks)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Bookmark",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
       // BookmarkScreen()
    }

}