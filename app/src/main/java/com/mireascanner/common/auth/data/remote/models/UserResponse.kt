package com.mireascanner.common.auth.data.remote.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UserResponse(
    val id: Long,
    val email: String
)