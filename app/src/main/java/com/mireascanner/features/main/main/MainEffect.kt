package com.mireascanner.features.main.main

sealed interface MainEffect {
    data class NavigateToIp(val ip: String) : MainEffect
}