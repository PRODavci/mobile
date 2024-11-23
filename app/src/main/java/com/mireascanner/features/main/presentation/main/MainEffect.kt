package com.mireascanner.features.main.presentation.main

sealed interface MainEffect {
    data class NavigateToIp(val ip: String) : MainEffect
}