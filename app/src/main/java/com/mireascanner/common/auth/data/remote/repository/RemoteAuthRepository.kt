package com.mireascanner.common.auth.data.remote.repository

import com.mireascanner.common.auth.data.remote.models.SignBody
import com.mireascanner.common.auth.data.remote.models.SignUpResponse
import com.mireascanner.common.auth.data.remote.models.UserResponse
import com.mireascanner.common.utils.Result

interface RemoteAuthRepository {

    suspend fun signUp(signBody: SignBody): Result<SignUpResponse>

    suspend fun getUserData(accessToken: String): Result<UserResponse>
}