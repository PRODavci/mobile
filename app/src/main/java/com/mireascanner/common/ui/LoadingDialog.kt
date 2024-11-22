package com.mireascanner.common.ui

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mireascanner.R

class LoadingDialog(context: Context) {
    private val dialog: Dialog = Dialog(context).apply {
        setCancelable(false)
        val view: View = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null)
        setContentView(view)
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}