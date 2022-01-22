package ru.arzonpay.android.ui.placeholder.loadstate.state

import androidx.annotation.AttrRes
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface

class MainLoadState(
    @AttrRes val backgroundColor: Int = android.R.attr.windowBackground
) : LoadStateInterface {

    override fun equals(other: Any?): Boolean {
        return other is MainLoadState
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}