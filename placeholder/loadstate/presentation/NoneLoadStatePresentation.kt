package ru.arzonpay.android.ui.placeholder.loadstate.presentation

import android.view.View
import ru.arzonpay.android.ui.placeholder.PlaceHolderViewContainer
import ru.arzonpay.android.ui.placeholder.loadstate.state.NoneLoadState
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.arzonpay.android.ui.placeholder.setClickableAndFocusable

/**
 * Представление состояния NoneState, скрывающее PlaceHolderViewContainer
 */
class NoneLoadStatePresentation(
    private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<NoneLoadState>() {
    private val view by lazy { View(placeHolder.context) }

    override fun showState(state: NoneLoadState) {
        with(placeHolder) {
            changeViewTo(view, true)
            setClickableAndFocusable(false)
        }
    }
}