package com.mireascanner.common.auth.data.local.repository

import android.content.SharedPreferences
import com.mireascanner.di.auth.EncryptedSharedPreferencesQualifier
import javax.inject.Inject

class LocalAuthRepositoryImpl @Inject constructor(@EncryptedSharedPreferencesQualifier private val encryptedSharedPreferences: SharedPreferences) :
    LocalAuthRepository {


    override suspend fun saveAccessAndRefreshTokens(accessToken: String, refreshToken: String) {
        encryptedSharedPreferences.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .putString(REFRESH_TOKEN, refreshToken)
            .apply()
    }

    companion object {

        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }
}