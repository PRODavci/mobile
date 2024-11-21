package com.mireascanner.features.auth.presentation.sign_up

import com.mireascanner.common.auth.domain.models.User
import java.lang.Exception

sealed interface SignUpState {

    data class Succeed(val user: User) : SignUpState

    data class Failed(val exception: Exception) : SignUpState

    data object Content : SignUpState

    data object Loading : SignUpState
}