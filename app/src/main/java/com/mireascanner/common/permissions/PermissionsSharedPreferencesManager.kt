package com.mireascanner.common.permissions

import android.content.Context

class PermissionsSharedPreferencesManager() {

    suspend fun putLocationPermissionFlag(context: Context, isRationaleShow: Boolean) {
        context.getSharedPreferences(PERMISSIONS_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit()
            .apply {
                putBoolean(IS_NOTIFICATIONS_EXPLANATION_DIALOG_RATIONALE_SHOW, isRationaleShow)
                apply()
            }
    }

    suspend fun getLocationPermissionFlag(context: Context): Boolean {
        return context.getSharedPreferences(PERMISSIONS_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            .getBoolean(IS_NOTIFICATIONS_EXPLANATION_DIALOG_RATIONALE_SHOW, false)
    }

    companion object {
        private const val PERMISSIONS_SHARED_PREFERENCES = "PERMISSIONS_SHARED_PREFERENCES"

        private const val IS_NOTIFICATIONS_EXPLANATION_DIALOG_RATIONALE_SHOW =
            "IS_NOTIFICATIONS_EXPLANATION_DIALOG_RATIONALE_SHOW"
    }
}