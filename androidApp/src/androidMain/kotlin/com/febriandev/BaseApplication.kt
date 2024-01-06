package com.febriandev

import android.app.Application
import com.febriandev.common.R
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        NotifierManager.initialize(
            NotificationPlatformConfiguration.Android(
            notificationIconResId = R.drawable.icon_app,
                notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData()
        ))

    }
}