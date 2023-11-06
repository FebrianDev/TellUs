import androidx.compose.material.Colors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import ui.screens.auth.RegisterScreen
import ui.screens.post.DetailPostScreen
import ui.screens.post.HomeScreen
import ui.screens.post.InsertPostScreen
import ui.themes.colorPrimary

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App() {
    MaterialTheme {
        Surface(
            contentColor = Color.White
        ) {
            Navigator(
                HomeScreen()
                //RegisterScreen()
               // DetailPostScreen(5)
               // InsertPostScreen()
            )

//            BottomSheetNavigator {
//                Navigator(InsertPostScreen())
//            }
        }
    }
}

expect fun getPlatformName(): String