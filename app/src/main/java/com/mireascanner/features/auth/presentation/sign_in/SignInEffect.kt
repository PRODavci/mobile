package com.mireascanner.features.auth.presentation.sign_in

import com.mireascanner.features.auth.presentation.sign_up.SignUpEffect

sealed interface SignInEffect {
    data object NavigateToMain : SignInEffect

    data object ShowLoading : SignInEffect

    data object HideLoadingDialog : SignInEffect
}