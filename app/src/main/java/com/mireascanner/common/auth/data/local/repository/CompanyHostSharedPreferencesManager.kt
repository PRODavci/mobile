package com.mireascanner.common.auth.data.local.repository

import android.content.Context
import com.mireascanner.common.utils.RetrofitCreator

class CompanyHostSharedPreferencesManager(private val context: Context) {

    fun updateHost(token: String) {
        RetrofitCreator.companyHost = HOST
        context.getSharedPreferences(COMPANY_HOST_TOKEN_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putString(HOST, token)
            .apply()
    }

    fun getHost(): String {
        val host = context.getSharedPreferences(COMPANY_HOST_TOKEN_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .getString(HOST, "")!!
        RetrofitCreator.companyHost = host
        return host
    }



    companion object {

        private const val COMPANY_HOST_TOKEN_SHARED_PREFERENCES =
            "COMPANY_HOST_TOKEN_SHARED_PREFERENCES"
        private const val HOST = "HOST"
    }
}