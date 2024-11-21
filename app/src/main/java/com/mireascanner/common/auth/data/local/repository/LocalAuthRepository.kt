package com.mireascanner.common.auth.data.local.repository

interface LocalAuthRepository {

    suspend fun saveAccessAndRefreshTokens(accessToken: String, refreshToken: String)
}