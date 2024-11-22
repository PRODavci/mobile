package com.mireascanner.common.auth.domain

import com.mireascanner.common.auth.domain.models.User
import com.mireascanner.common.utils.Result

interface AuthRepository {

    suspend fun signUp(email: String, password: String): Result<User>

    suspend fun signIn(email: String, password: String): Result<User>

    suspend fun checkUserDataSafely(): Result<User>
}