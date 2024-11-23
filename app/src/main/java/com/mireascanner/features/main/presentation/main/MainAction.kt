package com.mireascanner.features.main.presentation.main


sealed interface MainAction {
    data object GetAllScans : MainAction
}