package com.mireascanner.common.auth.data.local.repository

import com.mireascanner.common.utils.Result

interface LocalAuthRepository {

    suspend fun saveAccessAndRefreshTokens(accessToken: String, refreshToken: String)

    suspend fun getAccessToken(): Result<String>

    suspend fun getRefreshToken(): Result<String>
}