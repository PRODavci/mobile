package com.mireascanner.cloud_messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mireascanner.MainActivity
import com.mireascanner.R
import com.mireascanner.cloud_messaging.data.FirebaseTokenSharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FirebaseMessagingService : FirebaseMessagingService() {

    private val coroutineScope = CoroutineScope(SupervisorJob())

    private var firebaseTokenSharedPreferencesManager: FirebaseTokenSharedPreferencesManager? = null

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            showNotification(it.title, it.body)
        }
    }

    private fun showNotification(title: String?, messageBody: String?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATIONS_CHANNEL_ID,
                NOTIFICATIONS_CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATIONS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        firebaseTokenSharedPreferencesManager =
            FirebaseTokenSharedPreferencesManager(applicationContext)
        coroutineScope.launch(Dispatchers.IO) {
            firebaseTokenSharedPreferencesManager!!.updateToken(token)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        firebaseTokenSharedPreferencesManager = null
    }

    companion object {
        private const val NOTIFICATIONS_CHANNEL_ID = "FCM"
    }
}