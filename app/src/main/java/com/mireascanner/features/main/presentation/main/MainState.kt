package com.mireascanner.features.main.presentation.main

sealed interface MainState{
    data object Content : MainState

    data object Loading : MainState

}