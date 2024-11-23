package com.mireascanner.features.main.presentation.host_details

import com.mireascanner.common.main.data.remote.model.ScanDetailsResponse
import com.mireascanner.common.utils.UIText

data class HostDetailsState(
    val error: UIText? = null,
    val details: ScanDetailsResponse? = null
)