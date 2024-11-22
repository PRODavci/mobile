package com.mireascanner.common.auth.domain.usecase.validate_email

sealed interface ValidationEmailResult {
    data object Success : ValidationEmailResult
    data object Failed : ValidationEmailResult
}