package com.febriandev

import android.app.Application
import android.content.Context
import com.febriandev.common.R
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

class BaseApplication : Application() {

    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        NotifierManager.initialize(
            NotificationPlatformConfiguration.Android(
            notificationIconResId = R.drawable.icon_app,
                notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData()
        ))

    }
}