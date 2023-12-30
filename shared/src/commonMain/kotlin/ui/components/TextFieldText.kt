package ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ui.themes.colorPrimary

@Composable
fun TextFieldText(
    icon: ImageVector,
    text: String,
    placeholder: String,
    isError: Boolean,
    onTextChange: (text: String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 0.dp),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = placeholder
            )
        },
        value = text,
        placeholder = { Text(text = placeholder) },
        onValueChange = onTextChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorPrimary,
            cursorColor = colorPrimary,
            textColor = colorPrimary
        ),

        trailingIcon = {
            if (isError)
                Icon(Icons.Filled.Info, "Error", tint = Color.Red)
        },
        singleLine = true,
        isError = isError,
        textStyle = TextStyle(colorPrimary)
    )
}