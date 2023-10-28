package data.auth.model

@kotlinx.serialization.Serializable
data class AuthRequest(
    var email: String = "",
    var password: String = ""
)