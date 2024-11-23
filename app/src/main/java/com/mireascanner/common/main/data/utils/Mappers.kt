package com.mireascanner.common.main.data.utils

import com.mireascanner.common.main.data.remote.model.AllScansResponse
import com.mireascanner.common.main.domain.models.Scan

fun AllScansResponse.toDomain(): Array<Scan>{
    val scans = arrayListOf<Scan>()
    for(scanResponse in this.data){
        scans.add(Scan(
            timeStamp = scanResponse.timestamp,
            hosts = scanResponse.hosts.map { it.ip }
        ))
    }
    return scans.toTypedArray()
}