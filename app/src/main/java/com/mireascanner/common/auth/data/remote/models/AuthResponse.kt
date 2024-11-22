package com.mireascanner.common.auth.data.remote.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class AuthResponse(
    val user: UserResponse,
    val tokens: TokenResponse
)


