package ru.arzonpay.android.ui.util

import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.arzonpay.android.ui.view.extensions.getThemeColor

/**
 * Установка масштабирования лоадера во время свайпа.
 */
fun SwipeRefreshLayout.setScalableWhileSwipe(isScalable: Boolean) {
    setProgressViewOffset(isScalable, progressViewStartOffset, progressViewEndOffset)
}

/**
 * Задание цветов лоадера.
 *
 * @param progressColorRes идентификатор цветового ресурса для лоадера.
 * @param progressBackgroundColorRes идентификатор цветового ресурса для фона лоадера.
 */
fun SwipeRefreshLayout.colorize(
    @AttrRes progressColorRes: Int,
    @AttrRes progressBackgroundColorRes: Int
) {
    setProgressColorRes(progressColorRes)
    setProgressBackgroundColorRes(progressBackgroundColorRes)
}

/**
 * Задание цвета лоадера.
 *
 * @param colorId идентификатор цветового ресурса для лоадера.
 */
fun SwipeRefreshLayout.setProgressColorRes(@AttrRes colorId: Int) {
    setColorSchemeColors(context.getThemeColor(colorId))
}

/**
 * Задание фонового цвета лоадера.
 *
 * @param colorId идентификатор цветового ресурса для фона лоадера.
 */
fun SwipeRefreshLayout.setProgressBackgroundColorRes(@AttrRes colorId: Int) {
    setProgressBackgroundColorSchemeColor(context.getThemeColor(colorId))
}
