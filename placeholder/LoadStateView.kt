package ru.arzonpay.android.ui.placeholder

import ru.arzonpay.android.ui.placeholder.loadstate.renderer.DefaultLoadStateRenderer
import ru.arzonpay.android.ui.placeholder.loadstate.state.*
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface

/**
 * Интерфейс View, способной отображать load-стейты множества [LoadStateType].
 */
interface LoadStateView {

    val renderer: DefaultLoadStateRenderer?

    /**
     * Отрисовать [loadStateType] на экран.
     *
     * **Если передано null - смена состояния и отрисовка не будет произведена.**
     */
    fun renderLoadState(loadStateType: LoadStateType?) {
        renderer?.let {
            loadStateType?.getLoadState()?.also(it::render)
        }
    }

    fun getMainLoadState() = MainLoadState()

    fun getErrorLoadState() = ErrorLoadState()

    fun getEmptyLoadState() = EmptyLoadState()

    fun LoadStateType.getLoadState(): LoadStateInterface {
        return when (this) {
            is LoadStateType.None -> NONE
            is LoadStateType.Main -> getMainLoadState()
            is LoadStateType.Transparent -> TRANSPARENT
            is LoadStateType.Error -> {
                if (!titleText.isNullOrEmpty()) {
                    getErrorLoadState().copy(text = titleText)
                } else {
                    getErrorLoadState()
                }
            }
            is LoadStateType.Empty -> getEmptyLoadState()
        }
    }

    private companion object {

        val NONE = NoneLoadState()
        val TRANSPARENT = TransparentLoadState()
    }
}
