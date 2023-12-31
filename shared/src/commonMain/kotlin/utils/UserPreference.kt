package utils

object UserPreference {

    private val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()

    fun getIdUser(): String {
        return keyValueStorage.observableIdUser
    }

    fun getFcmToken(): String {
        return keyValueStorage.observableFCMToken
    }
}