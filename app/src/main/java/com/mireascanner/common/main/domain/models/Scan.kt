package com.mireascanner.common.main.domain.models

data class Scan(
    val timeStamp: String,
    val hosts: List<Host>
)