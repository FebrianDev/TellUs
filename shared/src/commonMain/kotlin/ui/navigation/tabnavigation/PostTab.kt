package ui.navigation.tabnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object PostTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.AddBox)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Post",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {

    }
}