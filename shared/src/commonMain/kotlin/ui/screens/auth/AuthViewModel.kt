package ui.screens.auth

import data.auth.AuthSdk
import data.auth.AuthState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {

    private val sdk: AuthSdk = AuthSdk()

    private val _authState = MutableStateFlow<Result<AuthState>>(Result.success(AuthState.Empty))
    val authState: StateFlow<Result<AuthState>> get() = _authState.asStateFlow()

    val auth = Firebase.auth

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
}