package utils

import kotlinx.coroutines.flow.Flow

interface KeyValueStorage {

    var idUser: String
    val observableIdUser: String

    var email:String

    var fcmToken: String
    val observableFCMToken: String

    var apiToken: String
    val observableApiToken: Flow<String>

    var isNotification: Boolean

    fun cleanStorage()
}

enum class StorageKeys {
    ID_USER, EMAIL, FCM_TOKEN, API_TOKEN, IS_NOTIFICATION;

    val key get() = this.name
}