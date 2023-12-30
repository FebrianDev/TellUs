package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.themes.colorPrimary

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EmptyState(
    image: String = "",
    text: String = ""
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SpacerH(16.dp)

        Icon(
            modifier = Modifier
                .padding(start = 6.dp)
                .width(128.dp)
                .height(128.dp),
            painter = painterResource(image),
            contentDescription = "Options",
            tint = colorPrimary
        )

        SpacerH(16.dp)

        TextSubtitleMedium(text, color = colorPrimary)
    }
}