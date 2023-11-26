package ui.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.themes.bgColor
import ui.themes.colorPrimary

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ItemReceiveChat() {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .padding(0.dp, 4.dp, 16.dp, 4.dp),
            shape = RoundedCornerShape(0.dp, 24.dp, 24.dp, 0.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = bgColor)

        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    "Hello World",
                    color = colorPrimary,
                    fontSize = 14.sp
                )

                androidx.compose.material.Text(
                    "14.35",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    minLines = 1,
                    textAlign = TextAlign.End
                )

            }
        }
    }
}