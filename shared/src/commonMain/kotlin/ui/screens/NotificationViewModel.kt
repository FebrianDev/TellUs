package ui.screens

import data.bookmark.network.NotificationRequest
import data.fcm.NotificationApi
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import utils.KeyValueStorage
import utils.KeyValueStorageImpl

class NotificationViewModel : ViewModel() {

    private val notificationApi = NotificationApi()
    private val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()

    fun sendNotification(notificationRequest: NotificationRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            notificationApi.sendNotification(notificationRequest)
        }
    }

    fun updateToken(idUser: String, token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken.collectLatest {
                notificationApi.updateToken(idUser, token, it)
            }
        }
    }

}