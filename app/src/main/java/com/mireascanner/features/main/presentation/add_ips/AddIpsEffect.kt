package com.mireascanner.features.main.presentation.add_ips

sealed interface AddIpsEffect {

    data object ShowLoading : AddIpsEffect

    data object HideLoading : AddIpsEffect

    data object NavigateBack : AddIpsEffect

    data object ShowError : AddIpsEffect

    data object NavigateToAuth : AddIpsEffect
}