package com.mireascanner.common.auth.domain

import com.mireascanner.common.auth.domain.models.User

interface AuthRepository {

    suspend fun signUp(email: String, password: String): Result<User>
}