package com.mireascanner.common.auth.domain

interface AuthRepository {

    suspend fun signUp()
}