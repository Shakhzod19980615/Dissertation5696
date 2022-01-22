package ru.arzonpay.android.ui.placeholder.loadstate.renderer

import ru.arzonpay.android.ui.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.core.mvp.loadstate.BaseLoadStateRenderer
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.arzonpay.android.ui.placeholder.loadstate.presentation.*
import ru.arzonpay.android.ui.placeholder.loadstate.state.*

/**
 * Проектная реализация BaseLoadStateRenderer
 */
class DefaultLoadStateRenderer(
    placeHolderView: PlaceHolderViewContainer,
    override val defaultState: LoadStateInterface = NoneLoadState()
) : BaseLoadStateRenderer() {
    init {
        putPresentation(NoneLoadState::class.java, NoneLoadStatePresentation(placeHolderView))
        putPresentation(EmptyLoadState::class.java, EmptyLoadStatePresentation(placeHolderView))
        putPresentation(ErrorLoadState::class.java, ErrorLoadStatePresentation(placeHolderView))
        putPresentation(MainLoadState::class.java, MainLoadingStatePresentation(placeHolderView))
        putPresentation(
            TransparentLoadState::class.java,
            TransparentLoadingStatePresentation(placeHolderView)
        )
    }
}