package com.mireascanner.features.main.presentation.main

sealed interface MainEffect {

    data object ShowLoader : MainEffect

    data object CancelLoader : MainEffect

    data object NavigateToAuth : MainEffect
}