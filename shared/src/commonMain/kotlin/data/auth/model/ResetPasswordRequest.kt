package data.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    var email: String,
    var code: String,
    var password: String
)