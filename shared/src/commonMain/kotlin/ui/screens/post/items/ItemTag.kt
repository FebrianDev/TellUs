package ui.screens.post.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItemTag(
    tag: String = "Random",
    onClick : () -> Unit
) {
    Card(
        modifier = Modifier.wrapContentSize().padding(4.dp).clickable (
            onClick = onClick)
        ,
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Text(tag, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}