package com.mireascanner.common.ui

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.mireascanner.R

fun showErrorSnackbar(context: Context, view: View, @StringRes stringResId: Int){
    Snackbar.make(
        view,
        stringResId,
        Snackbar.LENGTH_SHORT
    ).setBackgroundTint(context.getColor(R.color.md_theme_errorContainer_highContrast))
        .show()
}

fun showErrorSnackbar(context: Context, view: View, text: String){
    Snackbar.make(
        view,
        text,
        Snackbar.LENGTH_SHORT
    ).setBackgroundTint(context.getColor(R.color.md_theme_errorContainer_highContrast))
        .show()
}