package com.mireascanner.features.main.presentation.scan.presentation

sealed interface ScanEffect {
    data class NavigateToScanDetails(
        val scanId: String
    )
}