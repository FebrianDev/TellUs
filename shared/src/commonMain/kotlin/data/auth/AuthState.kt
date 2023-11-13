package data.auth

import data.auth.model.AuthResponse
import data.response.ApiResponse
import dev.gitlive.firebase.auth.AuthResult

sealed class AuthState {
    data class Success(val data: AuthResult) : AuthState()
    data class Error(val message: String) : AuthState()
    object Loading : AuthState()
    object Empty : AuthState()

}