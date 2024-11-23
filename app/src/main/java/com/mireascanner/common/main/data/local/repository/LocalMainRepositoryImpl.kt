package com.mireascanner.common.main.data.local.repository

import android.content.SharedPreferences
import com.mireascanner.common.auth.data.local.repository.LocalAuthRepositoryImpl
import com.mireascanner.common.auth.data.local.repository.LocalAuthRepositoryImpl.Companion
import com.mireascanner.common.exceptions.UnauthorizedException
import com.mireascanner.common.utils.Result
import com.mireascanner.di.auth.EncryptedSharedPreferencesQualifier
import javax.inject.Inject

class LocalMainRepositoryImpl @Inject constructor(
    @EncryptedSharedPreferencesQualifier private val encryptedSharedPreferences: SharedPreferences
) : LocalMainRepository {
    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }
    override suspend fun getAccessToken(): Result<String> {
        return try {
            val result = encryptedSharedPreferences.getString(ACCESS_TOKEN, "")
            if (!result.isNullOrBlank()) {
                Result.Success(result)
            } else {
                Result.Error(UnauthorizedException())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}