package com.mireascanner.common.main.data.remote.repository

import com.mireascanner.common.main.data.remote.model.AllScansResponse
import com.mireascanner.common.main.data.remote.model.ScanDetailsResponse
import com.mireascanner.common.utils.Result

interface RemoteMainRepository {

    suspend fun getAllScans(accessToken: String): Result<AllScansResponse>

    suspend fun getScanDetails(accessToken: String, scanId: Int): Result<ScanDetailsResponse>
}