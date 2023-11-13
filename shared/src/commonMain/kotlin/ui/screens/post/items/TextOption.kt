package ui.screens.post.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.themes.colorPrimary

@Composable
fun TextOption(
    text: String = "",
    onClick: () -> Unit = {}
) {

    Text(
        text = text,
        fontSize = 16.sp,
        color = colorPrimary,
        modifier = Modifier.padding(top = 8.dp).fillMaxWidth().clickable(onClick = onClick),
    )

    Divider(modifier = Modifier.padding(vertical = 4.dp))
}
