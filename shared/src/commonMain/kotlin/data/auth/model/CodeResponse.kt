package data.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class CodeResponse(
    var code: String = ""
)