package com.mireascanner.features.auth.presentation.sign_up

sealed interface SignUpEffect {

    data object NavigateToMain : SignUpEffect

    data object ShowLoading : SignUpEffect

    data object HideLoadingDialog : SignUpEffect

}