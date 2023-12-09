package ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.themes.colorPrimary

@Composable
fun DividerComposable(
    color: Color = colorPrimary,
    thickness: Dp = 1.dp,
    modifier: Modifier = Modifier.padding(vertical = 8.dp)
) {
    Divider(
        color = color,
        thickness = thickness,
        modifier = modifier
    )
}