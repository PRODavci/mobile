package com.mireascanner.common.auth.data.remote.repository

import android.util.Log
import com.mireascanner.common.auth.data.remote.models.SignBody
import com.mireascanner.common.auth.data.remote.models.SignUpResponse
import com.mireascanner.common.auth.data.remote.network.AuthNetworkService
import com.mireascanner.common.utils.Result
import javax.inject.Inject

class RemoteAuthRepositoryImpl @Inject constructor(private val authNetworkService: AuthNetworkService) :
    RemoteAuthRepository {

    override suspend fun signUp(signBody: SignBody): Result<SignUpResponse> {
        return try {
            val result = authNetworkService.signUp(signBody)
            if (result.code() == 200) {
                Result.Success(result.body()!!)
            } else {
                Result.Error(Exception())
            }
        } catch (e: Exception) {
            throw e
            Result.Error(e)
        }
    }
}