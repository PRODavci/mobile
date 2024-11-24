package com.mireascanner.common.main.data.remote.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class HostServiceResponse (
    val id: Int,
    val host_id: Int,
    val port: Int,
    val protocol: String,
    val name: String?,
    val product: String?,
    val version: String?,
    val conf: String?,
    val ostype: String?,
    val cves: List<VulnerabilityResponse>
)
