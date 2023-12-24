package ui.screens.post

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import ui.screens.MainScreen
import ui.screens.bookmark.BookmarkScreen
import ui.screens.chat.ChatScreen
import ui.screens.chat.ChatViewModel
import ui.screens.setting.SettingScreen
import ui.themes.bgColor
import ui.themes.colorPrimary
import ui.themes.colorSecondary
import utils.ScrollDirection
import utils.getUid

class HomeScreen : Screen {

    @Composable
    override fun Content() {

        val uid = getUid()

        val navigator = LocalNavigator.currentOrThrow

        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        val screens = listOf("Home", "Chat", "Post", "Bookmark", "Setting")
        var selectedScreen by remember { mutableStateOf(screens.first()) }

        var shouldHideBottomBar by remember { mutableStateOf(ScrollDirection.Down) }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = scaffoldState)
            },
            containerColor = bgColor,
            bottomBar = {

                AnimatedVisibility(
                    visible = shouldHideBottomBar == ScrollDirection.Up,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                ) {
                    BottomNavigation {
                        screens.forEach { screen ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        getIconForScreen(screen),
                                        contentDescription = screen,
                                        tint = if (screen == selectedScreen) colorPrimary else colorSecondary
                                    )
                                },
                                label = { Text("") },
                                selected = screen == selectedScreen,
                                onClick = { selectedScreen = screen },
                                modifier = Modifier.background(Color.White).padding(12.dp),
                                unselectedContentColor = colorSecondary,
                                selectedContentColor = colorPrimary
                            )
                        }
                    }
                }
            }
        ) {
            when (selectedScreen) {
                "Home" -> {
                    MainScreen(scaffoldState, coroutineScope){
                        shouldHideBottomBar = it
                    }
                }

                "Chat" -> {
                    ChatScreen(uid, scaffoldState, coroutineScope)
                }

                "Post" -> {
                    navigator.push(InsertPostScreen())
                }

                "Bookmark" -> {
                    BookmarkScreen(scaffoldState, coroutineScope)
                }

                "Setting" -> {
                    SettingScreen()
                }
            }
        }
    }

    @Composable
    fun getIconForScreen(screen: String): ImageVector {
        return when (screen) {
            "Home" -> Icons.Default.Home
            "Chat" -> Icons.Default.Chat
            "Post" -> Icons.Default.AddBox
            "Bookmark" -> Icons.Default.Bookmark
            "Setting" -> Icons.Default.Settings
            else -> Icons.Default.Home
        }
    }

}

