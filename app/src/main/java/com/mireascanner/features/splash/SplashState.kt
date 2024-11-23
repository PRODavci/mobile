package com.mireascanner.features.splash

sealed interface SplashState {

    data object Loading : SplashState

    data class ContentState(val isAuthorized: Boolean) : SplashState
}