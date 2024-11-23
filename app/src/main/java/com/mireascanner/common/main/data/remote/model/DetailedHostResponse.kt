package com.mireascanner.common.main.data.remote.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DetailedHostResponse (
    val id: Int,
    val ip: String,
    val services: List<HostServiceResponse>
)
