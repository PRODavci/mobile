package com.mireascanner.di.auth

import com.mireascanner.common.auth.domain.usecase.validate_email.ValidateEmailUseCase
import com.mireascanner.common.auth.domain.usecase.validate_password.ValidatePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UseCaseModule {
    @Singleton
    @Provides
    fun provideValidateEmailUseCase() = ValidateEmailUseCase()

    @Singleton
    @Provides
    fun provideValidatePasswordUseCase() = ValidatePasswordUseCase()
}