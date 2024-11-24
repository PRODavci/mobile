package com.mireascanner.common.auth.data.repository

import com.mireascanner.cloud_messaging.data.FirebaseTokenSharedPreferencesManager
import com.mireascanner.common.auth.data.local.repository.LocalAuthRepository
import com.mireascanner.common.auth.data.remote.models.SignBody
import com.mireascanner.common.auth.data.remote.models.TokenResponse
import com.mireascanner.common.auth.data.remote.repository.RemoteAuthRepository
import com.mireascanner.common.exceptions.UnauthorizedException
import com.mireascanner.common.auth.data.utils.toDomain
import com.mireascanner.common.auth.domain.AuthRepository
import com.mireascanner.common.auth.domain.models.User
import com.mireascanner.common.exceptions.InvalidCredentialsException
import com.mireascanner.common.utils.Result
import javax.inject.Inject
import kotlin.reflect.KSuspendFunction0

class AuthRepositoryImpl @Inject constructor(
    private val localAuthRepository: LocalAuthRepository,
    private val remoteAuthRepository: RemoteAuthRepository,
    private val firebaseTokenSharedPreferencesManager: FirebaseTokenSharedPreferencesManager
) : AuthRepository {

    override suspend fun signUp(email: String, password: String): Result<User> {
        val remoteResult = remoteAuthRepository.signUp(SignBody(email = email, password = password))
        return when (remoteResult) {
            is Result.Success -> {
                localAuthRepository.saveAccessAndRefreshTokens(
                    remoteResult.data.tokens.accessToken,
                    remoteResult.data.tokens.refreshToken
                )
                remoteAuthRepository.pushToken(
                    remoteResult.data.tokens.accessToken,
                    firebaseTokenSharedPreferencesManager.getToken()
                )
                Result.Success(remoteResult.data.user.toDomain())
            }

            is Result.Error -> {
                Result.Error(remoteResult.exception)
            }
        }
    }

    override suspend fun signIn(email: String, password: String): Result<User> {
        val remoteResult = remoteAuthRepository.signIn(SignBody(email = email, password = password))
        return when (remoteResult) {
            is Result.Success -> {
                localAuthRepository.saveAccessAndRefreshTokens(
                    remoteResult.data.tokens.accessToken,
                    remoteResult.data.tokens.refreshToken
                )
                remoteAuthRepository.pushToken(
                    remoteResult.data.tokens.accessToken,
                    firebaseTokenSharedPreferencesManager.getToken()
                )
                Result.Success(remoteResult.data.user.toDomain())
            }

            is Result.Error -> {
                Result.Error(remoteResult.exception)
            }
        }
    }

    override suspend fun checkUserDataSafely(): Result<User> {
        return when (val localResult = localAuthRepository.getAccessToken()) {
            is Result.Success -> {
                when (val remoteResult = remoteAuthRepository.getUserData(localResult.data)) {
                    is Result.Success -> {
                        Result.Success(remoteResult.data.toDomain())
                    }

                    is Result.Error -> {
                        if (remoteResult.exception is UnauthorizedException) {
                            updateTokensAndMakeCall(::checkUserDataSafely)
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

    private suspend fun <T> updateTokensAndMakeCall(call: KSuspendFunction0<Result<T>>): Result<T> {
        return when (val tokens = updateTokens()) {
            is Result.Success -> {
                call()
            }

            is Result.Error -> {
                Result.Error(tokens.exception)
            }
        }
    }

    private suspend fun updateTokens(): Result<TokenResponse> {
        return when (val localResult = localAuthRepository.getRefreshToken()) {
            is Result.Success -> {
                when (val remoteResult =
                    remoteAuthRepository.updateRefreshToken(localResult.data)) {
                    is Result.Success -> {
                        localAuthRepository.saveAccessAndRefreshTokens(
                            remoteResult.data.accessToken,
                            remoteResult.data.refreshToken
                        )
                        Result.Success(remoteResult.data)
                    }

                    is Result.Error -> {
                        Result.Error(remoteResult.exception)
                    }
                }
            }

            is Result.Error -> {
                Result.Error(localResult.exception)
            }
        }
    }
}