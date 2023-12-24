package utils

import kotlinx.coroutines.flow.Flow

interface KeyValueStorage {

    var idUser:String
    val observableIdUser: String

    var apiToken: String
    val observableApiToken: Flow<String>

    fun cleanStorage()
}

enum class StorageKeys {
    ID_USER, API_TOKEN;

    val key get() = this.name
}