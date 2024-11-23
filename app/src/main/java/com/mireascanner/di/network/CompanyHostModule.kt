package com.mireascanner.di.network

import android.content.Context
import com.mireascanner.common.auth.data.local.repository.CompanyHostSharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CompanyHostModule {

    @Provides
    fun provideCompanyHostSharedPreferencesManager(@ApplicationContext context: Context) =
        CompanyHostSharedPreferencesManager(context)
}