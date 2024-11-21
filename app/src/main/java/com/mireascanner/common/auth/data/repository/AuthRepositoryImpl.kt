package com.mireascanner.common.auth.data.repository

import com.mireascanner.common.auth.domain.AuthRepository
import com.mireascanner.common.auth.domain.models.User
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun signUp(email: String, password: String): Result<User> {
        TODO("Not yet implemented")
    }
}