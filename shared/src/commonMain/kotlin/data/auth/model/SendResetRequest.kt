package data.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class SendResetRequest(
    var email: String = "",
)