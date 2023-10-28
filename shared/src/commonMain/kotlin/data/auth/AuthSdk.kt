package data.auth

import data.auth.network.AuthApi

class AuthSdk {

    private val api = AuthApi()

    @Throws(Exception::class)
    suspend fun register(email: String, password: String): AuthState {
        return api.register(email, password)
    }

    @Throws(Exception::class)
    suspend fun login(email: String, password: String): AuthState {
        return api.login(email, password)
    }
}