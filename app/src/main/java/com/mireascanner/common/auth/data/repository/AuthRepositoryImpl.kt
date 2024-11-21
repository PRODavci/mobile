package com.mireascanner.common.auth.data.repository

import com.mireascanner.common.auth.data.local.repository.LocalAuthRepository
import com.mireascanner.common.auth.data.remote.models.SignBody
import com.mireascanner.common.auth.data.remote.repository.RemoteAuthRepository
import com.mireascanner.common.exceptions.UnauthorizedException
import com.mireascanner.common.auth.data.utils.toDomain
import com.mireascanner.common.auth.domain.AuthRepository
import com.mireascanner.common.auth.domain.models.User
import com.mireascanner.common.utils.Result
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val localAuthRepository: LocalAuthRepository,
    private val remoteAuthRepository: RemoteAuthRepository
) : AuthRepository {

    override suspend fun signUp(email: String, password: String): Result<User> {
        val remoteResult = remoteAuthRepository.signUp(SignBody(email = email, password = password))
        return when (remoteResult) {
            is Result.Success -> {
                localAuthRepository.saveAccessAndRefreshTokens(
                    remoteResult.data.tokens.accessToken,
                    remoteResult.data.tokens.refreshToken
                )
                Result.Success(remoteResult.data.user.toDomain())
            }

            is Result.Error -> {
                Result.Error(remoteResult.exception)
            }
        }
    }

    override suspend fun checkUserData(): Result<User> {
        return when (val localResult = localAuthRepository.getAccessToken()) {
            is Result.Success -> {
                when (val remoteResult = remoteAuthRepository.getUserData(localResult.data)) {
                    is Result.Success -> {
                        Result.Success(remoteResult.data.toDomain())
                    }

                    is Result.Error -> {
                        if (remoteResult.exception is UnauthorizedException) {
                            TODO()
                        } else {
                            Result.Error(remoteResult.exception)
                        }
                    }
                }
            }

            is Result.Error -> {
                Result.Error(localResult.exception)
            }
        }
    }
}