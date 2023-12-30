package ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.auth.CodeResetState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.components.BtnRounded
import ui.components.ProgressBarLoading
import ui.components.SpacerH
import ui.components.TextFieldText
import ui.components.TitleHeader
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.showSnackBar

class ForgotPassword : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val authViewModel = getViewModel(Unit, viewModelFactory { AuthViewModel() })

        var textEmail by remember { mutableStateOf("") }
        var isEmailError by remember { mutableStateOf(false) }

        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        Scaffold(
            snackbarHost =
            {
                SnackbarHost(hostState = scaffoldState)
            },
            containerColor = bgColor
        )
        {
            Column(modifier = Modifier.fillMaxSize()) {
                TitleHeader("Forgot Password", navigator)
                SpacerH(24.dp)

                TextFieldText(
                    Icons.Default.Email,
                    textEmail,
                    "Email",
                    isEmailError
                ) {
                    textEmail = it
                }

                SpacerH(24.dp)

                BtnRounded("Send Email") {
                    if (textEmail.isEmpty()) {
                        showSnackBar("Email must be filled", coroutineScope, scaffoldState)
                        isEmailError = true
                    } else if (!textEmail.contains("@") && !textEmail.contains(".")) {
                        showSnackBar(
                            "Email is not valid",
                            coroutineScope,
                            scaffoldState
                        )
                        isEmailError = true
                    } else {
                        authViewModel.sendResetPassword(textEmail)
                    }
                }
            }
        }

        authViewModel.codeResetState.collectAsState().value.onSuccess {
            when (it) {
                is CodeResetState.Loading -> {
                    ProgressBarLoading()
                }

                is CodeResetState.Error -> {
                    showSnackBar(it.message, coroutineScope, scaffoldState)
                }

                is CodeResetState.Success -> {
                    coroutineScope.launch {
                        val job = coroutineScope.launch {
                            scaffoldState.showSnackbar(
                                message = "Check your email! The code verification has been sent successfully",
                                duration = SnackbarDuration.Indefinite
                            )
                        }

                        delay(1000)
                        navigator.push(ResetPassword(textEmail))
                        job.cancel()
                    }
                }

                else -> {}
            }
        }.onFailure {
            showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
        }
    }
}
