package ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.auth.AuthState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.components.BtnRounded
import ui.components.ProgressBarLoading
import ui.components.SpacerH
import ui.components.TextFieldPassword
import ui.components.TextFieldText
import ui.screens.post.HomeScreen
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.KeyValueStorage
import utils.KeyValueStorageImpl
import utils.showSnackBar

class LoginScreen : Screen {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val authViewModel = getViewModel(Unit, viewModelFactory { AuthViewModel() })

        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        var textEmail by remember { mutableStateOf("") }
        var textPassword by remember { mutableStateOf("") }

        var isEmailError by remember { mutableStateOf(false) }
        var isPasswordError by remember {
            mutableStateOf(false)
        }

        val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = scaffoldState)
            },
            containerColor = bgColor
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Login",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = colorPrimary,
                )

                SpacerH(32.dp)

                //Image
                Image(
                    painter = painterResource("drawable/icon_app.png"),
                    contentDescription = "Icon App"
                )

                SpacerH(32.dp)

                TextFieldText(
                    Icons.Default.Email,
                    textEmail,
                    "Email",
                    isEmailError
                ) {
                    textEmail = it
                }

                SpacerH(12.dp)

                TextFieldPassword(textPassword, isPasswordError) {
                    textPassword = it
                }

                SpacerH(12.dp)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(24.dp, 0.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Forgot Password? ",
                        color = colorPrimary,
                    )

                    Text(
                        text = "Click Here",
                        color = colorPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navigator.push(ForgotPassword())
                        }
                    )
                }

                SpacerH(24.dp)

                BtnRounded("Login") {
                    if (textEmail.isEmpty()) {
                        showSnackBar("Email must be filled", coroutineScope, scaffoldState)
                        isEmailError = true
                        isPasswordError = false
                    } else if (!textEmail.contains("@") && !textEmail.contains(".")) {
                        showSnackBar(
                            "Email is not valid",
                            coroutineScope,
                            scaffoldState
                        )
                        isEmailError = true
                        isPasswordError = false
                    } else if (textPassword.isEmpty()) {
                        showSnackBar(
                            "Password must be filled",
                            coroutineScope,
                            scaffoldState
                        )
                        isEmailError = false
                        isPasswordError = true
                    } else if (textPassword.length < 6) {
                        showSnackBar(
                            "Password must be consist of at least 6 characters",
                            coroutineScope,
                            scaffoldState
                        )
                        isEmailError = false
                        isPasswordError = true
                    } else {
                        isEmailError = false
                        isPasswordError = false

                        authViewModel.login(textEmail, textPassword)
                    }

                    authViewModel.login(textEmail, textPassword)
                }

                SpacerH(12.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Do you have not an account? ",
                        color = colorPrimary,
                    )

                    Text(
                        text = "Register",
                        color = colorPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navigator.push(RegisterScreen())
                        }
                    )
                }
            }
        }

        authViewModel.authState.collectAsState().value.onSuccess {
            when (it) {
                is AuthState.Loading -> {
                    ProgressBarLoading()
                }

                is AuthState.Error -> {
                    showSnackBar(it.message, coroutineScope, scaffoldState)
                }

                is AuthState.Success -> {
                    val data = it.data.data
                    keyValueStorage.idUser = data?.id.toString()
                    keyValueStorage.apiToken = data?.token.toString()
                    navigator.push(HomeScreen())
                }

                else -> {

                }
            }
        }.onFailure {
            showSnackBar(it.message.toString(), coroutineScope, scaffoldState)
        }

    }
}