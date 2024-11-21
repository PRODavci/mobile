package com.mireascanner.di.auth

import com.mireascanner.common.auth.data.local.repository.LocalAuthRepository
import com.mireascanner.common.auth.data.local.repository.LocalAuthRepositoryImpl
import com.mireascanner.common.auth.data.remote.repository.RemoteAuthRepository
import com.mireascanner.common.auth.data.remote.repository.RemoteAuthRepositoryImpl
import com.mireascanner.common.auth.data.repository.AuthRepositoryImpl
import com.mireascanner.common.auth.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindLocalAuthRepository(localAuthRepositoryImpl: LocalAuthRepositoryImpl): LocalAuthRepository

    @Binds
    abstract fun bindRemoteAuthRepository(remoteAuthRepositoryImpl: RemoteAuthRepositoryImpl): RemoteAuthRepository
}