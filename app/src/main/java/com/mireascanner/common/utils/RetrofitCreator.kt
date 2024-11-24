package com.mireascanner.common.utils

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mireascanner.common.auth.data.remote.network.AuthNetworkService
import com.mireascanner.common.main.data.remote.network.MainNetworkService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitCreator {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    private fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(companyHost)
        .client(provideOkHttpClient())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    fun provideAuthNetworkService(): AuthNetworkService =
        provideRetrofit().create(AuthNetworkService::class.java)

    fun provideMainNetworkService(): MainNetworkService =
        provideRetrofit().create(MainNetworkService::class.java)

    var companyHost = "https://next-rabbit-legal.ngrok-free.app/api/v1/"
}