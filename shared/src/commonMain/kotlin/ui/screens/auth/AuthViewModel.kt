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

    private val _authState = MutableStateFlow<AuthState>(AuthState.Empty)
    val authState: StateFlow<AuthState> get() = _authState.asStateFlow()

    val auth = Firebase.auth

    fun register(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            _authState.value =
                AuthState.Success(auth.createUserWithEmailAndPassword(email, password))
        }
    }

    fun login(email: String, password: String) {
         _authState.value = AuthState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _authState.value = AuthState.Success(auth.signInWithEmailAndPassword(email, password))
        }
    }
}