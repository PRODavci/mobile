package com.mireascanner.common.auth.data.remote.repository

import com.mireascanner.common.auth.data.remote.models.SignBody
import com.mireascanner.common.auth.data.remote.models.AuthResponse
import com.mireascanner.common.auth.data.remote.models.TokenResponse
import com.mireascanner.common.auth.data.remote.models.UserResponse
import com.mireascanner.common.utils.Result

interface RemoteAuthRepository {

    suspend fun signUp(signBody: SignBody): Result<AuthResponse>

    suspend fun signIn(signBody: SignBody): Result<AuthResponse>

    suspend fun getUserData(accessToken: String): Result<UserResponse>

    suspend fun updateRefreshToken(refreshToken: String): Result<TokenResponse>
}