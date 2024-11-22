package com.mireascanner.common.auth.domain.usecase.validate_email

import android.util.Patterns

class ValidateEmailUseCase {
    operator fun invoke(email: String): ValidationEmailResult{
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationEmailResult.Failed
        }
        return ValidationEmailResult.Success
    }
}