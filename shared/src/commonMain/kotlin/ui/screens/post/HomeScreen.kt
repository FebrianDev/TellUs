package ui.screens.post

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.launch
import ui.components.TopBar
import ui.screens.post.tabs.BestPostScreen
import ui.screens.post.tabs.LatestPostScreen
import ui.screens.post.tabs.MyPostScreen
import ui.screens.post.tabs.TabRowItem
import ui.themes.colorPrimary

class HomeScreen : Screen {

    @Composable
    override fun Content() {
//        TabNavigator(HomeTab) {
//            Scaffold(
//                content = {
//                    CurrentTab()
//                },
//                bottomBar = {
//                    BottomNavigation {
//                        TabNavigationItem(HomeTab)
//                        TabNavigationItem(ChatTab)
//                        TabNavigationItem(PostTab)
//                        TabNavigationItem(BookmarkTab)
//                        TabNavigationItem(SettingTab)
//                        TabNavigationItem(DetailPostTab)
//                    }
//                }
//            )
//        }

        val postViewModel = getViewModel(Unit, viewModelFactory { PostViewModel() })

        Column(
            modifier = Modifier.fillMaxWidth().wrapContentSize().background(Color.White)
        ) {
            TopBar("Home")
            TabLayout(postViewModel)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TabLayout(
        postViewModel: PostViewModel
    ) {

        val coroutineScope = rememberCoroutineScope()

        val tabRowItems = listOf(
            TabRowItem(
                title = "Latest",
                screen = { LatestPostScreen(postViewModel) }
            ),
            TabRowItem(
                title = "Best",
                screen = { BestPostScreen(postViewModel) }
            ),
            TabRowItem(
                title = "Mine",
                screen = { MyPostScreen(postViewModel) }
            ))

        val pagerState = rememberPagerState {
            tabRowItems.size
        }

        TabRow(
            modifier = Modifier.padding(horizontal = 0.dp).widthIn(min = 24.dp),
            divider = {
                TabRowDefaults.Divider(
                    color = Color.White
                )
            },
            indicator = {},
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.White
        ) {
            tabRowItems.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 0.dp)
                        .clip(CircleShape)
                        .background(if (pagerState.currentPage == index) colorPrimary else Color.White),
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = {
                        Text(
                            text = item.title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            color = if (pagerState.currentPage == index) Color.White else colorPrimary
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

}
