package com.febriandev

import MainView
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.febriandev.common.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.permissionUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.initialize(this)

        val permissionUtil by permissionUtil()
        permissionUtil.askNotificationPermission()

        setContent {
            MainView(this)
        }
    }
}
