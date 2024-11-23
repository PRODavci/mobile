package com.mireascanner.common.main.data.repository

import com.mireascanner.common.main.data.local.repository.LocalMainRepository
import com.mireascanner.common.main.data.remote.repository.RemoteMainRepository
import com.mireascanner.common.main.domain.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val localMainRepository: LocalMainRepository,
    private val remoteMainRepository: RemoteMainRepository
) : MainRepository {
}