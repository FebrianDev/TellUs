package utils

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.getStringFlow
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow

class KeyValueStorageImpl : KeyValueStorage {

    private val settings: Settings by lazy { Settings() }
    private val observableSettings: ObservableSettings by lazy { settings as ObservableSettings }
    override var idUser: String
        get() = settings[StorageKeys.ID_USER.key] ?: ""
        set(value) {
            settings[StorageKeys.ID_USER.key] = value
        }

    override val observableIdUser: String
        get() = observableSettings.getString(StorageKeys.ID_USER.key, "")
    override var email: String
        get() = settings[StorageKeys.EMAIL.key] ?: ""
        set(value) {
            settings[StorageKeys.EMAIL.key] = value
        }
    override var fcmToken: String
        get() = settings[StorageKeys.FCM_TOKEN.key] ?: ""
        set(value) {
            settings[StorageKeys.FCM_TOKEN.key] = value
        }
    override val observableFCMToken: String
        get() = settings.getString(StorageKeys.FCM_TOKEN.key, "")


    override var apiToken: String
        get() = settings[StorageKeys.API_TOKEN.key] ?: ""
        set(value) {
            settings[StorageKeys.API_TOKEN.key] = value
        }

    @OptIn(ExperimentalSettingsApi::class)
    override val observableApiToken: Flow<String>
        get() = observableSettings.getStringFlow(StorageKeys.API_TOKEN.key, "")

    override var isNotification: Boolean
        get() = settings[StorageKeys.IS_NOTIFICATION.key] ?: true
        set(value) {
            settings[StorageKeys.IS_NOTIFICATION.key] = value
        }

    override fun cleanStorage() {
        settings.clear()
    }


}