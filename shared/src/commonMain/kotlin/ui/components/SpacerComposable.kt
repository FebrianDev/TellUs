package ui.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SpacerH(height: IntrinsicSize) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun SpacerW(width: IntrinsicSize) {
    Spacer(modifier = Modifier.width(width))
}