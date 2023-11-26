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
    override var uid: String?
        get() = settings[StorageKeys.UID.key]
        set(value) {
            settings[StorageKeys.UID.key] = value
        }
    @OptIn(ExperimentalSettingsApi::class)
    override val observableUid: Flow<String>
        get() = observableSettings.getStringFlow(StorageKeys.UID.key, "")

    override fun cleanStorage() {
        settings.clear()
    }


}