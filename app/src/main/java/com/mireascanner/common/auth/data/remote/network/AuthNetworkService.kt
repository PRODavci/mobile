package com.mireascanner.common.auth.data.remote.network

import com.mireascanner.common.auth.data.remote.models.SignBody
import com.mireascanner.common.auth.data.remote.models.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthNetworkService {

    @POST("/")
    suspend fun signUp(@Body signBody: SignBody): Response<SignUpResponse>
}