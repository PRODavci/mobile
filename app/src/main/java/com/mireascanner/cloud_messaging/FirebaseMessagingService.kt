package com.mireascanner.cloud_messaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mireascanner.cloud_messaging.data.FirebaseTokenSharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FirebaseMessagingService : FirebaseMessagingService() {

    private val coroutineScope = CoroutineScope(SupervisorJob())

    private val firebaseTokenSharedPreferencesManager = FirebaseTokenSharedPreferencesManager()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        coroutineScope.launch(Dispatchers.IO) {
            firebaseTokenSharedPreferencesManager.updateToken(applicationContext, token)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}