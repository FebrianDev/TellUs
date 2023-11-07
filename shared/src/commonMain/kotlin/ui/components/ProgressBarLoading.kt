package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.themes.colorPrimary

@Composable
fun ProgressBarLoading() {
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
        CircularProgressIndicator(
            color = colorPrimary
        )
    }
}