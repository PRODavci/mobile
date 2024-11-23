package com.mireascanner.features.main.presentation.main

import com.mireascanner.common.main.domain.models.Scan
import com.mireascanner.common.utils.UIText

data class MainState(
    val error: UIText? = null,
    val scans: Array<Scan>? = null
)