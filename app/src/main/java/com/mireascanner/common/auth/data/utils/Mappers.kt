package com.mireascanner.common.auth.data.utils

import com.mireascanner.common.auth.data.remote.models.UserResponse
import com.mireascanner.common.auth.domain.models.User

fun UserResponse.toDomain() = User(email)