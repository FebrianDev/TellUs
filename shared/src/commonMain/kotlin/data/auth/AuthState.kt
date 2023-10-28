package data.auth

import data.auth.model.AuthResponse
import data.response.ApiResponse

sealed class AuthState {
    data class Success(val data: ApiResponse<AuthResponse>) : AuthState()
    data class Error(val message: String) : AuthState()
    object Loading : AuthState()
    object Empty : AuthState()

}