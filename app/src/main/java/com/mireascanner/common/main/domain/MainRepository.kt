package com.mireascanner.common.main.domain

import com.mireascanner.common.main.data.remote.model.AllScansResponse
import com.mireascanner.common.main.data.remote.model.ScanDetailsResponse
import com.mireascanner.common.main.data.remote.model.StartScanBody
import com.mireascanner.common.utils.Result

interface MainRepository {
    suspend fun getAllScans(): Result<AllScansResponse>

    suspend fun getScanDetails(scanId: Int): Result<ScanDetailsResponse>

    suspend fun startScan(networks: List<String>): Result<StartScanBody>
}