package com.mireascanner.features.auth.presentation.sign_in

import com.mireascanner.common.utils.UIText

data class SignInState(
    val emailError: UIText? = null,
    val error: UIText? = null,
    val isSignInButtonEnabled: Boolean = false
)