package com.mireascanner.common.main.data.remote.repository

import com.mireascanner.common.exceptions.UnauthorizedException
import com.mireascanner.common.main.data.remote.model.AllScansResponse
import com.mireascanner.common.main.data.remote.model.ScanDetailsResponse
import com.mireascanner.common.main.data.remote.model.StartScanBody
import com.mireascanner.common.main.data.remote.network.MainNetworkService
import com.mireascanner.common.utils.Result
import javax.inject.Inject


class RemoteMainRepositoryImpl @Inject constructor(
    private val mainNetworkService: MainNetworkService
) : RemoteMainRepository {
    override suspend fun getAllScans(
        accessToken: String
    ): Result<AllScansResponse> {
        return try {
            val result = mainNetworkService.getAllScans(accessToken)
            if (result.code() == 200) {
                Result.Success(result.body()!!)
            }else if(result.code() == 401){
                Result.Error(UnauthorizedException())
            } else{
                Result.Error(Exception())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getScanDetails(
        accessToken: String,
        scanId: Int
    ): Result<ScanDetailsResponse> {
        return try {
            val result = mainNetworkService.getScanDetails(accessToken, scanId)
            if (result.code() == 200) {
                Result.Success(result.body()!!)
            } else {
                Result.Error(Exception())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun startScan(
        accessToken: String,
        networks: List<String>
    ): Result<StartScanBody> {
        return try {
            val result = mainNetworkService.startScan(accessToken, StartScanBody(networks))
            if (result.code() == 200) {
                Result.Success(result.body()!!)
            }else{
                Result.Error(Exception())
            }
        }catch (e: Exception){
            Result.Error(e)
        }
    }

}