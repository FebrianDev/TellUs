package data.fcm

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    var token:String
)