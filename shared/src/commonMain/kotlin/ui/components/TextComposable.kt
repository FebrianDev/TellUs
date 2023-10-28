package ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

fun TextTitle(text: String) {

}

@Composable
fun TextSubtitleMedium(text: String = "", textAlign: TextAlign = TextAlign.Center) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        textAlign = textAlign,
        fontSize = 20.sp,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun TextBodyBold(text: String = "", modifier: Modifier = Modifier.fillMaxWidth()) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun TextBodyMedium(text: String, textAlign: TextAlign = TextAlign.Start) {
    Text(
        text = text,
        fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = textAlign
    )
}