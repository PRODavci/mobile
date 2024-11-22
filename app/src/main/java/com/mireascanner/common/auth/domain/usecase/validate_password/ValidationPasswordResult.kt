package com.mireascanner.common.auth.domain.usecase.validate_password

sealed class ValidationPasswordResult {
    data object Success : ValidationPasswordResult()

    data object VeryShortPassword : ValidationPasswordResult()

    data object NotContainsLettersOrDigits : ValidationPasswordResult()

    data object NotContainsLowerOrUpperCase : ValidationPasswordResult()
}