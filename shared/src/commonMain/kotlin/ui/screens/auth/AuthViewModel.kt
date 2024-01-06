package ui.screens.auth

import data.auth.AuthSdk
import data.auth.AuthState
import data.auth.CodeResetState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val sdk: AuthSdk = AuthSdk()

    private val _authState = MutableStateFlow<Result<AuthState>>(Result.success(AuthState.Empty))
    val authState: StateFlow<Result<AuthState>> get() = _authState.asStateFlow()

    private val _codeResetState =
        MutableStateFlow<Result<CodeResetState>>(Result.success(CodeResetState.Empty))
    val codeResetState: StateFlow<Result<CodeResetState>> get() = _codeResetState.asStateFlow()


    fun register(email: String, password: String) {
        _authState.value = Result.success(AuthState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _authState.value = sdk.register(email, password)
        }
    }

    fun login(email: String, password: String) {
        _authState.value = Result.success(AuthState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _authState.value = sdk.login(email, password)
        }
    }

    fun sendResetPassword(email: String) {
        _codeResetState.value = Result.success(CodeResetState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _codeResetState.value = sdk.sendEmail(email)
        }
    }

    fun resetPassword(email: String, code: String, password: String) {
        _authState.value = Result.success(AuthState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _authState.value = sdk.resetPassword(email, code, password)
        }
    }

    fun getApiToken(idUser: String) {
        _authState.value = Result.success(AuthState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            _authState.value = sdk.getApiToken(idUser)
        }
    }
}