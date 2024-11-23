package com.mireascanner.features.main.presentation.host_details

sealed interface HostDetailsEffect {
    data object ShowLoader : HostDetailsEffect

    data object CancelLoader : HostDetailsEffect
}