package data.auth

import data.auth.network.AuthApi

class AuthSdk {

    private val api = AuthApi()

    @Throws(Exception::class)
    suspend fun register(email: String, password: String): Result<AuthState> {
        return try {
            Result.success(api.register(email, password))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    suspend fun login(email: String, password: String): Result<AuthState> {
        return try {
            Result.success(api.login(email, password))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}