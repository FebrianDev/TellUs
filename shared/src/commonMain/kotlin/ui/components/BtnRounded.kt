package ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.themes.bgColor
import ui.themes.colorPrimary

@Composable
fun BtnRounded(
    text: String = "",
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .padding(24.dp, 0.dp),
    enable : Boolean = true,
    onClick: () -> Unit = {},
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorPrimary),
        enabled = enable
    ) {
        Text(
            text = text,
            color = bgColor,
            fontWeight = FontWeight.Bold,
        )
    }
}