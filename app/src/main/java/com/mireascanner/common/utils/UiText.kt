package com.mireascanner.common.utils


import android.content.Context
import androidx.annotation.StringRes

sealed class UIText {

    data class DynamicText(val value: String) : UIText()

    class StringResource(@StringRes val resId: Int, vararg val args: Any) : UIText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicText -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}