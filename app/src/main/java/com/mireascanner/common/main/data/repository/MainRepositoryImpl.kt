package com.mireascanner.common.main.data.repository

import com.mireascanner.common.main.data.local.repository.LocalMainRepository
import com.mireascanner.common.main.data.remote.model.AllScansResponse
import com.mireascanner.common.main.data.remote.model.ScanDetailsResponse
import com.mireascanner.common.main.data.remote.repository.RemoteMainRepository
import com.mireascanner.common.main.domain.MainRepository
import com.mireascanner.common.utils.Result
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val localMainRepository: LocalMainRepository,
    private val remoteMainRepository: RemoteMainRepository
) : MainRepository {
    override suspend fun getAllScans(): Result<AllScansResponse> {
        return when(val localResult = localMainRepository.getAccessToken()){
            is Result.Success -> {
                when(val remoteResult = remoteMainRepository.getAllScans(localResult.data)){
                    is Result.Success -> {
                        Result.Success(remoteResult.data)
                    }

                    is Result.Error -> {
                        Result.Error(remoteResult.exception)
                    }
                }
            }
            is Result.Error -> {
                Result.Error(localResult.exception)
            }
        }
    }

    override suspend fun getScanDetails(scanId: Int): Result<ScanDetailsResponse> {
        return when(val localResult = localMainRepository.getAccessToken()){
            is Result.Success -> {
                when(val remoteResult = remoteMainRepository.getScanDetails(localResult.data, scanId)){
                    is Result.Success -> {
                        Result.Success(remoteResult.data)
                    }

                    is Result.Error -> {
                        Result.Error(remoteResult.exception)
                    }
                }
            }
            is Result.Error -> {
                Result.Error(localResult.exception)
            }
        }
    }

}