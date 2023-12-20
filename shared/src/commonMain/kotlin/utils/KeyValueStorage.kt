package utils

import kotlinx.coroutines.flow.Flow

interface KeyValueStorage {

    var apiToken: String?

    val observableApiToken: Flow<String>?

    fun cleanStorage()
}

enum class StorageKeys {
    API_TOKEN;

    val key get() = this.name
}