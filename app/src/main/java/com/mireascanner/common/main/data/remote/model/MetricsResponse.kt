package com.mireascanner.common.main.data.remote.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MetricsResponse(
    val base_score: Float,
)