package com.mireascanner.features.auth.presentation.sign_in

sealed interface SignInAction {
    data class SignIn(val email: String, val password: String) : SignInAction

    data class EmailChanged(val email: String) : SignInAction

    data class PasswordChanged(val password: String) : SignInAction
}