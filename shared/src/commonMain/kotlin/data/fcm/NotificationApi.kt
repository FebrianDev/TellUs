package data.fcm

import data.bookmark.network.NotificationRequest
import data.utils.Constant
import data.utils.Constant.SERVER_KEY
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NotificationApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun sendNotification(notificationRequest: NotificationRequest) {
        client.post("https://fcm.googleapis.com/fcm/send") {
            contentType(ContentType.Application.Json)
            setBody(notificationRequest)
            headers {
                append(
                    "Authorization",
                    SERVER_KEY
                )
            }
        }
    }

    suspend fun updateToken(idUser: String, token: String, apiToken: String) {
        client.put("${Constant.BASE_URL}/api/token/${idUser}") {
            contentType(ContentType.Application.Json)
            setBody(TokenRequest(token))
            headers {
                append(
                    "api-token",
                    apiToken
                )
            }
        }
    }

}