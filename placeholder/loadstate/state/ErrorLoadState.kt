package ru.arzonpay.android.ui.placeholder.loadstate.state

import androidx.annotation.AttrRes
import ru.arzonpay.android.base_feature.R
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

data class ErrorLoadState(
    val text: String = EMPTY_STRING,
    val actionText: String = EMPTY_STRING,
    @AttrRes val backgroundColor: Int = android.R.attr.windowBackground,
    @AttrRes val iconRes: Int = R.attr.errorStateIcon,
    val action: () -> Unit = {}
) : LoadStateInterface {

    override fun equals(other: Any?): Boolean {
        return other is ErrorLoadState
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
