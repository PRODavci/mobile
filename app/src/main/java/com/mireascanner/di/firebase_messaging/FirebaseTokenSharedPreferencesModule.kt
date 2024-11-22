package com.mireascanner.di.firebase_messaging

import android.content.Context
import com.mireascanner.cloud_messaging.data.FirebaseTokenSharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class FirebaseTokenSharedPreferencesModule {

    @Provides
    fun provideFirebaseTokenSharedPreferencesManager(@ApplicationContext context: Context) =
        FirebaseTokenSharedPreferencesManager(context)
}