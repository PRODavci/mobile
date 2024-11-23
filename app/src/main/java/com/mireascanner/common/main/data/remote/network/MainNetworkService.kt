package com.mireascanner.common.main.data.remote.network

import com.mireascanner.common.main.data.remote.model.AllScansResponse
import com.mireascanner.common.main.data.remote.model.ScanDetailsResponse
import com.mireascanner.common.main.data.remote.model.StartScanBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface MainNetworkService {
    companion object {
        private const val AUTHORIZATION = "Authorization"
    }

    @GET("scans")
    suspend fun getAllScans(
        @Header(AUTHORIZATION) accessToken: String
    ): Response<AllScansResponse>

    @GET("scans/{scanId}")
    suspend fun getScanDetails(
        @Header(AUTHORIZATION) accessToken: String,
        @Path("scanId") scanId: Int
    ): Response<ScanDetailsResponse>

    @POST("scans/start")
    suspend fun startScan(
        @Header(AUTHORIZATION) accessToken: String,
        @Body startScanBody: StartScanBody
    ): Response<StartScanBody>

}