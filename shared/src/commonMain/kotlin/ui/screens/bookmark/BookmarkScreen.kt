package ui.screens.bookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import ui.components.TopBar

@Composable
fun BookmarkScreen(
) {

    val bookmarkViewModel = getViewModel(Unit, viewModelFactory { BookmarkViewModel() })


    Column(
        modifier = Modifier.fillMaxWidth().wrapContentSize()
    ) {
        TopBar("Bookmark")
    }
}