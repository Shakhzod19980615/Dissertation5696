package ru.arzonpay.android.ui.placeholder.loadstate.state

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface

class TransparentLoadState : LoadStateInterface {

    override fun equals(other: Any?): Boolean {
        return other is TransparentLoadState
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}