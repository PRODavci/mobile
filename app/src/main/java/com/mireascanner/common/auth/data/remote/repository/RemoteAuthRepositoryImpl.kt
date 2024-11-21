package com.mireascanner.common.auth.data.remote.repository

import com.mireascanner.common.auth.data.remote.models.SignBody
import com.mireascanner.common.auth.data.remote.models.SignUpResponse

class RemoteAuthRepositoryImpl : RemoteAuthRepository {
    override suspend fun signUp(signBody: SignBody): Result<SignUpResponse> {
        TODO("Not yet implemented")
    }
}