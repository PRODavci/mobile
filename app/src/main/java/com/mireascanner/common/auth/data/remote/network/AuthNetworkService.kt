package com.mireascanner.common.auth.data.remote.network

import com.mireascanner.common.auth.data.remote.models.SignBody
import com.mireascanner.common.auth.data.remote.models.SignUpResponse
import com.mireascanner.common.auth.data.remote.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthNetworkService {

    @POST("users")
    suspend fun signUp(@Body signBody: SignBody): Response<SignUpResponse>

    @GET("users/me")
    suspend fun getUserData(@Header(AUTHORIZATION) accessToken: String): Response<UserResponse>

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }
}