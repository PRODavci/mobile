package com.mireascanner.common.main.data.remote.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ScanResponse (
    val id: Int,
    val network: String,
    val timestamp: String,
    val hosts: List<HostResponse>
)
