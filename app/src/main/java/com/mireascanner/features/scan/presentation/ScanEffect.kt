package com.mireascanner.features.scan.presentation

sealed interface ScanEffect {
    data class NavigateToScanDetails(
        val scanId: String
    )
}