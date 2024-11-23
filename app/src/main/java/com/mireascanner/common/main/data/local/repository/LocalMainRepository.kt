package com.mireascanner.common.main.data.local.repository

import com.mireascanner.common.utils.Result

interface LocalMainRepository {

    suspend fun getAccessToken(): Result<String>
}