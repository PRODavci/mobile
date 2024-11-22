package com.mireascanner.features.auth.presentation.sign_up

import com.mireascanner.common.auth.domain.models.User
import com.mireascanner.common.auth.domain.usecase.validate_password.ValidationPasswordResult
import com.mireascanner.common.utils.UIText
import java.lang.Exception

data class SignUpState(
    val passwordError: UIText? = null,
    val emailError: UIText? = null,
    val isSignUpButtonAvailable: Boolean = false,
    val error: UIText? = null,
)