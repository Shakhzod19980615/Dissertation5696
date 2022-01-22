package ru.arzonpay.android.ui.placeholder.loadstate.presentation

import android.view.LayoutInflater
import android.view.View
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.arzonpay.android.ui.placeholder.PlaceHolderViewContainer
import ru.arzonpay.android.ui.placeholder.loadstate.state.MainLoadState
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.ui.placeholder.setClickableAndFocusable

/**
 * Представление состояния MainLoading в виде ProgressBar
 */
class MainLoadingStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<MainLoadState>() {

    private val view: View by lazy {
        LayoutInflater.from(placeHolder.context).inflate(R.layout.layout_load_state, placeHolder, false)
    }

    override fun showState(state: MainLoadState) {
        with(placeHolder) {
            changeViewTo(view)
            setClickableAndFocusable(true)
            show()
        }
    }
}