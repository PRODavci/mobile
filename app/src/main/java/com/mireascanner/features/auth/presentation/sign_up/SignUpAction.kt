package com.mireascanner.features.auth.presentation.sign_up

sealed interface SignUpAction {
    data class SignUp(val email: String, val password: String) : SignUpAction

    data class EmailChanged(val email: String) : SignUpAction

    data class PasswordChanged(val password: String) : SignUpAction
}