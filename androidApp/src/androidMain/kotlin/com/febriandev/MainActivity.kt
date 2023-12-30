package com.febriandev

import MainView
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.febriandev.common.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.initialize(this)

        NotifierManager.initialize(NotificationPlatformConfiguration.Android(
            notificationIconResId = R.drawable.icon_app
        ))

//        NotifierManager.initialize(
//            configuration = NotificationPlatformConfiguration.Android(
//                notificationIconResId = R.drawable.ic_launcher_foreground,
//            )
//        )

        setContent {
            MainView()
//            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
//                    return@OnCompleteListener
//                }
//                // Get new FCM registration token
//                val token = task.result
//                Log.d("myToken", "" + token)
//            })
//            Firebase.messaging.isAutoInitEnabled = true


        }
    }

}
