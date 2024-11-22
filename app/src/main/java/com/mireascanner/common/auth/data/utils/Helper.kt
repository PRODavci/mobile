package com.mireascanner.common.auth.data.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.mireascanner.R

object Helper {
    fun showErrorSnackBar(view: View, @StringRes textId: Int) {
        Snackbar.make(
            view,
            view.context.getString(textId),
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(view.context.getColor(R.color.md_theme_errorContainer_highContrast))
            .show()
    }

    fun showErrorSnackBar(view: View, text: String) {
        Snackbar.make(
            view,
            text,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(view.context.getColor(R.color.md_theme_errorContainer_highContrast))
            .show()
    }
}