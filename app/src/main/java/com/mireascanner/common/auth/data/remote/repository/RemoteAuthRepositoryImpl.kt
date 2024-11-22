package com.mireascanner.common.auth.data.remote.repository

import com.mireascanner.common.auth.data.remote.models.RefreshTokenBody
import com.mireascanner.common.auth.data.remote.models.SignBody
import com.mireascanner.common.auth.data.remote.models.AuthResponse
import com.mireascanner.common.auth.data.remote.models.TokenResponse
import com.mireascanner.common.auth.data.remote.models.UserResponse
import com.mireascanner.common.auth.data.remote.network.AuthNetworkService
import com.mireascanner.common.exceptions.InvalidCredentialsException
import com.mireascanner.common.exceptions.UnauthorizedException
import com.mireascanner.common.utils.Result
import javax.inject.Inject

class RemoteAuthRepositoryImpl @Inject constructor(private val authNetworkService: AuthNetworkService) :
    RemoteAuthRepository {

    override suspend fun signUp(signBody: SignBody): Result<AuthResponse> {
        return try {
            val result = authNetworkService.signUp(signBody)
            if (result.code() == 200) {
                Result.Success(result.body()!!)
            }else{
                Result.Error(Exception())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun signIn(signBody: SignBody): Result<AuthResponse>{
        return try {
            val result = authNetworkService.signIn(signBody)
            if(result.code() == 200){
                Result.Success(result.body()!!)
            }else if(result.code() == 401){
                Result.Error(InvalidCredentialsException())
            }else{
                Result.Error(Exception())
            }
        }catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun getUserData(accessToken: String): Result<UserResponse> {
        return try {
            val result = authNetworkService.getUserData(accessToken)
            if (result.code() == 200) {
                Result.Success(result.body()!!)
            } else if (result.code() == 401) {
                Result.Error(UnauthorizedException())
            } else {
                Result.Error(Exception())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateRefreshToken(refreshToken: String): Result<TokenResponse> {
        return try {
            val result = authNetworkService.updateTokens(RefreshTokenBody(refreshToken))
            if (result.code() == 200) {
                Result.Success(result.body()!!)
            } else if (result.code() == 401) {
                Result.Error(UnauthorizedException())
            } else {
                Result.Error(Exception())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}