package com.mireascanner.cloud_messaging.data

import android.content.Context

class FirebaseTokenSharedPreferencesManager {

    fun updateToken(context: Context, token: String) {
        context.getSharedPreferences(FIREBASE_TOKEN_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putString(TOKEN, token)
            .apply()
    }

    fun getToken(context: Context): String? =
        context.getSharedPreferences(FIREBASE_TOKEN_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .getString(TOKEN, null)


    companion object {

        private const val FIREBASE_TOKEN_SHARED_PREFERENCES = "FIREBASE_TOKEN_SHARED_PREFERENCES"
        private const val TOKEN = "TOKEN"
    }
}