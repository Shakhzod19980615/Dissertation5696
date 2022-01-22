package ru.arzonpay.android.ui.placeholder.loadstate.presentation

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import ru.arzonpay.android.ui.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.ui.placeholder.loadstate.state.TransparentLoadState
import ru.arzonpay.android.ui.placeholder.setClickableAndFocusable

/**
 * Представление состояния TransparentLoading в виде ProgressBar поверх затемненного фона
 */
class TransparentLoadingStatePresentation(
    private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<TransparentLoadState>() {

    @ColorInt
    var transparentBackgroundColor =
        ContextCompat.getColor(placeHolder.context, R.color.transparent_bg_color)

    val view: View by lazy {
        LayoutInflater.from(placeHolder.context).inflate(
            R.layout.layout_state_transparent,
            placeHolder,
            false
        )
    }

    override fun showState(state: TransparentLoadState) {
        with(placeHolder) {
            setBackgroundColor(transparentBackgroundColor)
            changeViewTo(view)
            setClickableAndFocusable(true)
            show()
        }
    }
}