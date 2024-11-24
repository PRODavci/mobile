package com.mireascanner.features.main.presentation.host_details

sealed interface HostDetailsAction {
    data class GetHostDetails(val hostId: Int, val scanId: Int) : HostDetailsAction
}