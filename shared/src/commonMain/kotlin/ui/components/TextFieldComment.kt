package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import ui.themes.bgColor

@Composable
fun TextFieldComment(
    textComment: TextFieldValue,
    onComment: (textComment: TextFieldValue) -> Unit
) {
    TextField(
        value = textComment,
        onValueChange = {
            onComment.invoke(it)
        },
        placeholder = { Text(text = "Type your message") },
        modifier = Modifier.fillMaxWidth(0.8f).background(bgColor),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = bgColor,
            unfocusedContainerColor = bgColor,
            disabledContainerColor = bgColor,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
    )
}