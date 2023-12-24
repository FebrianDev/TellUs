package ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.components.TopBar
import ui.screens.post.PostViewModel
import ui.screens.post.tabs.BestPostScreen
import ui.screens.post.tabs.LatestPostScreen
import ui.screens.post.tabs.MyPostScreen
import ui.screens.post.tabs.TabRowItem
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.ScrollDirection

@Composable
fun MainScreen(
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onShowHideBottomBar: (shouldHideBottomBar: ScrollDirection) -> Unit
) {

    val postViewModel = getViewModel(Unit, viewModelFactory { PostViewModel() })

    Column(
        modifier = Modifier.fillMaxWidth().wrapContentSize()
    ) {
        TopBar("Home")
        TabLayout(postViewModel, scaffoldState, coroutineScope, onShowHideBottomBar)
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(
    postViewModel: PostViewModel,
    scaffoldState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onShowHideBottomBar: (shouldHideBottomBar: ScrollDirection) -> Unit
) {

    val tabRowItems = listOf(
        TabRowItem(
            title = "Latest",
            screen = {
                LatestPostScreen(
                    postViewModel, scaffoldState,
                    coroutineScope
                ) {
                    onShowHideBottomBar.invoke(it)
                }
            }
        ),
        TabRowItem(
            title = "Best",
            screen = {
                BestPostScreen(
                    postViewModel, scaffoldState,
                    coroutineScope, onShowHideBottomBar
                )
            }
        ),
        TabRowItem(
            title = "Mine",
            screen = {
                MyPostScreen(
                    postViewModel, scaffoldState,
                    coroutineScope, onShowHideBottomBar
                )
            }
        ))

    val pagerState = rememberPagerState {
        tabRowItems.size
    }

    TabRow(
        modifier = Modifier.padding(horizontal = 0.dp).widthIn(min = 24.dp),
        divider = {
            TabRowDefaults.Divider(
                color = bgColor
            )
        },
        indicator = {},
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = bgColor
    ) {
        tabRowItems.forEachIndexed { index, item ->
            Tab(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 0.dp)
                    .clip(CircleShape)
                    .background(if (pagerState.currentPage == index) colorPrimary else bgColor),
                selected = pagerState.currentPage == index,
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                text = {
                    Text(
                        text = item.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = if (pagerState.currentPage == index) bgColor else colorPrimary
                    )
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalPager(
        beyondBoundsPageCount = tabRowItems.size,
        state = pagerState
    ) {
        tabRowItems[pagerState.currentPage].screen()
    }

}