package ru.arzonpay.android.ui.placeholder.loadstate.state

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface

class NoneLoadState : LoadStateInterface {

    override fun equals(other: Any?): Boolean {
        return other is NoneLoadState
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}