package data.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    var id: Int = 0,
    var token: String = ""
)