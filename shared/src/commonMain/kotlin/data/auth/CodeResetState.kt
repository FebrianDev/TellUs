package data.auth

import data.auth.model.CodeResponse
import data.response.ApiResponse

sealed class CodeResetState {
    data class Success(val data: ApiResponse<CodeResponse>) : CodeResetState()
    data class Error(val message: String) : CodeResetState()
    object Loading : CodeResetState()
    object Empty : CodeResetState()

}