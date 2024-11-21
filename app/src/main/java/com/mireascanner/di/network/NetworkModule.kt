package com.mireascanner.di.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mireascanner.BuildConfig
import com.mireascanner.common.auth.data.remote.network.AuthNetworkService
import com.mireascanner.common.utils.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    fun provideAuthNetworkService(retrofit: Retrofit): AuthNetworkService =
        retrofit.create(AuthNetworkService::class.java)
}