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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
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
import ui.screens.post.HomeScreen
import ui.themes.bgColor
import ui.themes.colorPrimary
import utils.KeyValueStorage
import utils.KeyValueStorageImpl
import utils.showSnackBar

class LoginScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val authViewModel = getViewModel(Unit, viewModelFactory { AuthViewModel() })

        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        var textEmail: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
        var textPassword: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
        var passwordVisible by remember { mutableStateOf(false) }

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
                Spacer(modifier = Modifier.height(32.dp))

                //Image
                Image(
                    painter = painterResource("drawable/icon_app.png"),
                    contentDescription = "Icon App"
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 0.dp),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "emailIcon"
                        )
                    },
                    value = textEmail,
                    placeholder = { androidx.compose.material.Text(text = "Email") },
                    onValueChange = { newText ->
                        textEmail = newText
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorPrimary,
                        cursorColor = colorPrimary,
                        textColor = colorPrimary
                    ),

                    trailingIcon = {
                        if (isEmailError)
                            Icon(Icons.Filled.Info, "Error", tint = Color.Red)
                    },
                    singleLine = true,
                    isError = isEmailError,
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 0.dp),

                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "passwordIcon"
                        )
                    },
                    value = textPassword,
                    placeholder = { androidx.compose.material.Text(text = "Password") },
                    onValueChange = { newText ->
                        textPassword = newText
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorPrimary,
                        cursorColor = colorPrimary,
                        textColor = colorPrimary
                    ),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        // Localized description for accessibility services
                        val description =
                            if (passwordVisible) "Hide password" else "Show password"

                        // Toggle button to hide or display password
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }

                        if (isPasswordError)
                            Icon(Icons.Filled.Info, "Error", tint = Color.Red)
                    },
                    isError = isPasswordError,
                    textStyle = TextStyle(colorPrimary)
                )

                Spacer(modifier = Modifier.height(12.dp))

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

                        }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                BtnRounded("Login") {
                    if (textEmail.text.isEmpty()) {
                        showSnackBar("Email must be filled", coroutineScope, scaffoldState)
                        isEmailError = true
                        isPasswordError = false
                    } else if (!textEmail.text.contains("@") && !textEmail.text.contains(".")) {
                        showSnackBar(
                            "Email is not valid",
                            coroutineScope,
                            scaffoldState
                        )
                        isEmailError = true
                        isPasswordError = false
                    } else if (textPassword.text.isEmpty()) {
                        showSnackBar(
                            "Password must be filled",
                            coroutineScope,
                            scaffoldState
                        )
                        isEmailError = false
                        isPasswordError = true
                    } else if (textPassword.text.length < 6) {
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

                        authViewModel.login(textEmail.text, textPassword.text)
                    }

                    authViewModel.login("febrian26022001@gmail.com", "psjk1234")
                }

                Spacer(modifier = Modifier.height(12.dp))

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

        val authState = authViewModel.authState.collectAsState()

        authState.value.onSuccess {
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