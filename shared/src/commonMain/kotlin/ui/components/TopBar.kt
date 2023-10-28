package ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.themes.colorPrimary

@Composable
fun TopBar(
    title : String
) {
    TopAppBar(
        title = {
            Text(
                text =title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colorPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        backgroundColor = Color.White,
        contentColor = Color.White,
        elevation = 4.dp
    )
}