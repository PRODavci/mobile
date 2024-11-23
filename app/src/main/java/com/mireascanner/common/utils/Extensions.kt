package com.mireascanner.common.utils

import android.view.View

private var lastClickTime: Long = 0
private const val CLICK_TIME_INTERVAL: Long = 300

fun View.setOnClickListenerSafely(onClick: (View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= CLICK_TIME_INTERVAL) {
            lastClickTime = currentTime
            onClick(it)
        }
    }
}