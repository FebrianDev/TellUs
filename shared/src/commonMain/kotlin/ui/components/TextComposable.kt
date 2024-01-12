package ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ui.themes.colorPrimary

fun TextTitle(text: String) {

}

@Composable
fun TextSubtitleMedium(
    text: String = "",
    textAlign: TextAlign = TextAlign.Center,
    color: Color = Color.Black
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        textAlign = textAlign,
        fontSize = 20.sp,
        modifier = Modifier.fillMaxWidth(),
        color = color
    )
}

@Composable
fun TextBodyBold(
    text: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    color: Color = Color.Black
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
        color = color
    )
}

@Composable
fun TextSmallBold(
    text: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    color: Color = Color.Black
) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
        color = color
    )
}


@Composable
fun TextBodyMedium(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    color: Color = colorPrimary,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    Text(
        text = text,
        fontSize = 16.sp,
        modifier = modifier,
        textAlign = textAlign,
        color = colorPrimary
    )
}