package utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun showSnackBar(s: String, coroutineScope: CoroutineScope, scaffoldState: SnackbarHostState) {
    coroutineScope.launch {
        scaffoldState.showSnackbar(
            message = s,
            duration = SnackbarDuration.Short
        )
    }
}