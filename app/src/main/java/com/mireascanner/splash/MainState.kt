package com.mireascanner.splash

sealed interface MainState {

    data object Loading : MainState

    data class ContentState(val isAuthorized: Boolean) : MainState
}