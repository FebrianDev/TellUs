package data.auth.model

@kotlinx.serialization.Serializable
data class User(
    var email: String = "",
    var password: String = ""
)
