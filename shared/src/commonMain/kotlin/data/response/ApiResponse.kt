package data.response

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    var code: Int = 0,
    var status: String = "",
    var data: T? = null
)

@Serializable
data class ApiErrorResponse(
    var status: String = "",
    var code: Int = 0,
    var message: String = ""
)