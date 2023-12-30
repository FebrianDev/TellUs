package ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.auth.AuthState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.components.BtnRounded
import ui.components.SpacerH
import ui.components.TextFieldPassword
import ui.components.TextFieldText
import ui.components.TitleHeader
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.showSnackBar

class ResetPassword(private val email: String) : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val authViewModel = getViewModel(Unit, viewModelFactory { AuthViewModel() })

        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        var textPassword by remember { mutableStateOf("") }
        var isPassword by remember { mutableStateOf(false) }

        var textConfirmPassword by remember { mutableStateOf("") }
        var isConfirmPassword by remember { mutableStateOf(false) }

        var textCode by remember { mutableStateOf("") }
        var isCodeError by remember { mutableStateOf(false) }

        Scaffold(
            snackbarHost =
            {
                SnackbarHost(hostState = scaffoldState)
            },
            containerColor = bgColor
        )
        {
            Column(modifier = Modifier.fillMaxSize()) {

                TitleHeader("New Password", navigator)

                SpacerH(24.dp)

                TextFieldText(
                    Icons.Default.Key,
                    textCode,
                    "Code",
                    isCodeError
                ) {
                    textCode = it
                }

                SpacerH(16.dp)

                TextFieldPassword(
                    textPassword,
                    isPassword,
                    "New Password"
                ) {
                    textPassword = it
                }

                SpacerH(16.dp)

                TextFieldPassword(
                    textConfirmPassword,
                    isConfirmPassword,
                    "Confirm Password",
                ) {
                    textConfirmPassword = it
                }

                SpacerH(24.dp)

                BtnRounded("Reset Password") {
                    if (textCode.isEmpty()) {
                        showSnackBar("Code must be filled", coroutineScope, scaffoldState)
                        isCodeError = true
                        isPassword = false
                        isConfirmPassword = false
                    } else if (textPassword.isEmpty()) {
                        showSnackBar(
                            "Password must be filled",
                            coroutineScope,
                            scaffoldState
                        )
                        isCodeError = false
                        isPassword = true
                        isConfirmPassword = false
                    } else if (textPassword.length < 6) {
                        showSnackBar(
                            "Password must be consist of at least 6 characters",
                            coroutineScope,
                            scaffoldState
                        )
                        isCodeError = false
                        isPassword = true
                        isConfirmPassword = false
                    } else if (textConfirmPassword != textPassword) {
                        showSnackBar(
                            "Password is not match",
                            coroutineScope,
                            scaffoldState
                        )
                        isCodeError = false
                        isPassword = false
                        isConfirmPassword = true
                    } else {
                        isCodeError = false
                        isPassword = false
                        isConfirmPassword = false
                        authViewModel.resetPassword(email, textCode, textPassword)
                    }
                }
            }
        }

        authViewModel.authState.collectAsState().value.onSuccess {
            when (it) {
                is AuthState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = colorPrimary
                        )
                    }
                }

                is AuthState.Error -> {
                    showSnackBar(it.message, coroutineScope, scaffoldState)
                }

                is AuthState.Success -> {
                    coroutineScope.launch {
                        val job = coroutineScope.launch {
                            scaffoldState.showSnackbar(
                                message = "Success change password",
                                duration = SnackbarDuration.Indefinite
                            )
                        }

                        delay(1000)
                        navigator.push(LoginScreen())
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