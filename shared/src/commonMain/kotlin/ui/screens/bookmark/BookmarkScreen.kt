package ui.screens.bookmark

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.bookmark.model.BookmarkRequest
import data.bookmark.state.ListBookmarkState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import ui.components.EmptyState
import ui.components.ProgressBarLoading
import ui.components.TopBar
import ui.screens.post.OptionPostEvent
import ui.screens.post.items.ItemPostBookmark
import ui.themes.bgColor
import utils.getUid
import utils.showSnackBar

@Composable
fun BookmarkScreen(
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {

    val bookmarkViewModel = getViewModel(Unit, viewModelFactory { BookmarkViewModel() })

    val uid = getUid()

    val listEmptyBookmark by remember { mutableStateOf(arrayListOf<Boolean>()) }

    LaunchedEffect(uid) {
        bookmarkViewModel.getAllBookmark(uid)
    }

    val event = OptionPostEvent()

    Scaffold(
        containerColor = bgColor
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            TopBar("Bookmark")

            //handle network
            bookmarkViewModel.listBookmarkState.collectAsState().value.onSuccess {
                when (it) {
                    is ListBookmarkState.Loading -> {
                        ProgressBarLoading()
                    }

                    is ListBookmarkState.Error -> {
                        showSnackBar("Something was wrong!", coroutineScope, scaffoldState)
                    }

                    is ListBookmarkState.Success -> {
                        val listPost by remember { mutableStateOf(it.data.data?.toMutableStateList()) }
                        listPost?.toList()?.forEach { post ->
                            val empty =
                                post.Bookmarks.filter { bookmark -> bookmark.id_user == uid }
                            listEmptyBookmark.add(empty.isEmpty())
                        }

                        if (!listEmptyBookmark.contains(false) || listEmptyBookmark.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(bottom = 128.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                EmptyState("drawable/ic_no_bookmark.png", "No Bookmark")
                            }
                        } else {
                            LazyColumn(modifier = Modifier.padding(bottom = 64.dp, top = 8.dp)) {
                                items(listPost ?: listOf()) { data ->
                                    if (data.Bookmarks.isNotEmpty()) {
                                        if (data.Bookmarks.any { bookmark -> bookmark.id_user == uid })
                                            ItemPostBookmark(
                                                data,
                                                uid,
                                                coroutineScope,
                                                scaffoldState,
                                                event,
                                                onBookmarkPost = {
                                                    listPost?.remove(data)
                                                    bookmarkViewModel.insertBookmark(
                                                        BookmarkRequest(data.id, uid)
                                                    )

                                                    showSnackBar(
                                                        "Cancel Add to Bookmark",
                                                        coroutineScope,
                                                        scaffoldState
                                                    )
                                                })
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }.onFailure {
                showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
            }

        }
    }
}