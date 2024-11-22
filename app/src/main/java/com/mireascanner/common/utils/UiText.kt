package com.mireascanner.common.utils


import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class UIText {

    data class DynamicText(val value: String) : UIText()

    class StringResource(@StringRes val resId: Int, vararg val args: Any) : UIText()

    class PluralsResource(@PluralsRes val resId: Int, val quantity: Int, vararg val args: Any) :
        UIText()
}