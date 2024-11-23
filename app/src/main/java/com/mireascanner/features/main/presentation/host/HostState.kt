package com.mireascanner.features.main.presentation.host

sealed interface HostState {
    data object Content : HostState

    data object Loading : HostState

    data object Failed : HostState
}