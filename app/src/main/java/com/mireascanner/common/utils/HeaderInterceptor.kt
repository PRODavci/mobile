package com.mireascanner.common.utils

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()
        if (original.headers.names().contains(AUTHORIZATION)) {
            builder.header(AUTHORIZATION, "Bearer " + original.headers[AUTHORIZATION]!!)
        }
        return chain.proceed(builder.method(original.method, original.body).build())
    }



    companion object {
        private const val AUTHORIZATION = "Authorization"
    }
}