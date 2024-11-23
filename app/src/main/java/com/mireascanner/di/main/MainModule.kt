package com.mireascanner.di.main

import com.mireascanner.common.main.data.local.repository.LocalMainRepository
import com.mireascanner.common.main.data.local.repository.LocalMainRepositoryImpl
import com.mireascanner.common.main.data.remote.repository.RemoteMainRepository
import com.mireascanner.common.main.data.remote.repository.RemoteMainRepositoryImpl
import com.mireascanner.common.main.data.repository.MainRepositoryImpl
import com.mireascanner.common.main.domain.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {

    @Binds
    abstract fun bindMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository

    @Binds
    abstract fun bindLocalMainRepository(localMainRepositoryImpl: LocalMainRepositoryImpl): LocalMainRepository

    @Binds
    abstract fun bindRemoteMainRepository(remoteMainRepositoryImpl: RemoteMainRepositoryImpl): RemoteMainRepository
}