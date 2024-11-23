package com.mireascanner.features.main.main

sealed interface MainState{
    data object Content : MainState

    data object Loading : MainState

}