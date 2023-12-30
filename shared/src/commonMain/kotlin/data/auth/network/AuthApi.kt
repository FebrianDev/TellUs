package data.auth.network

import data.auth.AuthState
import data.auth.CodeResetState
import data.auth.model.AuthRequest
import data.auth.model.ResetPasswordRequest
import data.auth.model.SendResetRequest
import data.response.ApiErrorResponse
import data.utils.Constant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class AuthApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun register(email: String, password: String): AuthState {
        val data = client.post("${Constant.BASE_URL}/api/register") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(email = email, password = password))
        }
        var authState: AuthState = AuthState.Empty
        when (data.status.value) {
            200, 201 -> {
                authState = AuthState.Success(data.body())
            }

            in 400..404 -> {
                authState = AuthState.Error((data.body() as ApiErrorResponse).message.toString())
            }
        }

        return authState
    }

    suspend fun login(email: String, password: String): AuthState {
        val data = client.post("${Constant.BASE_URL}/api/login") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(email = email, password = password))
        }

        var authState: AuthState = AuthState.Empty

        when (data.status.value) {
            200, 201 -> {
                authState = AuthState.Success(data.body())
            }

            in 400..404 -> {
                authState = AuthState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return authState
    }

    suspend fun sendEmail(email: String): CodeResetState {
        val data = client.post("${Constant.BASE_URL}/api/send-reset") {
            contentType(ContentType.Application.Json)
            setBody(SendResetRequest(email = email))
        }

        var codeResetState: CodeResetState = CodeResetState.Empty

        when (data.status.value) {
            200, 201 -> {
                codeResetState = CodeResetState.Success(data.body())
            }

            in 400..404 -> {
                codeResetState = CodeResetState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return codeResetState
    }

    suspend fun resetPassword(email: String, code: String, password: String): AuthState {
        val data = client.post("${Constant.BASE_URL}/api/reset-password") {
            contentType(ContentType.Application.Json)
            setBody(ResetPasswordRequest(email, code, password))
        }

        var authState: AuthState = AuthState.Empty

        when (data.status.value) {
            200, 201 -> {
                authState = AuthState.Success(data.body())
            }

            in 400..404 -> {
                authState = AuthState.Error((data.body() as ApiErrorResponse).message)
            }
        }

        return authState
    }
}