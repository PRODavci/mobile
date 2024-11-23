package com.mireascanner.common.main.data.remote.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
class ScanDetailsResponse (
    val id: Int,
    val network: String,
    val timestamp: String,
    val hosts: List<DetailedHostResponse>
)