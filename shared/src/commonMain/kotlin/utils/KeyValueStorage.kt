package utils

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

interface KeyValueStorage {

    var uid: String?

    val observableUid: Flow<String>?

    fun cleanStorage()
}

enum class StorageKeys {
    UID;

    val key get() = this.name
}