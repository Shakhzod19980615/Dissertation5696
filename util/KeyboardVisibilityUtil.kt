package ru.arzonpay.android.ui.util

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver

/**
 * Обертка для отслеживания открытия и закрытия клавиатуры
 */
object KeyboardVisibilityUtil {

    private var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    /**
     * Отслеживание видимости клавиатуры
     * !!!Важно, нужно очистить методом removeKeyboardVisibilityListener
     */
    fun keyboardVisibilityListener(
        decorView: View?,
        isKeyboardVisibilityChangedAction: (isKeyboardVisible: Boolean) -> Unit = {}
    ) {
        keyboardVisibilityListener(
            isKeyboardVisibleAction = { isKeyboardVisibilityChangedAction(true) },
            isKeyboardGoneAction = { isKeyboardVisibilityChangedAction(false) },
            decorView = decorView
        )
    }

    /**
     * Отслеживание видимости клавиатуры
     * !!!Важно, нужно очистить методом removeKeyboardVisibilityListener
     */
    fun keyboardVisibilityListener(
        isKeyboardVisibleAction: (Int) -> Unit = {},
        isKeyboardGoneAction: (Int) -> Unit = {},
        decorView: View?
    ) {
        onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            decorView?.apply {
                val r = Rect()
                decorView.getWindowVisibleDisplayFrame(r)
                val height = decorView.context.resources.displayMetrics.heightPixels
                // Если diff = 0 клавиатура закрыта, иначе открыта
                val diff = height - r.bottom
                if (diff <= 0) {
                    isKeyboardGoneAction(diff)
                } else {
                    isKeyboardVisibleAction(diff)
                }
            }
        }
        decorView?.viewTreeObserver?.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun removeKeyboardVisibilityListener(decorView: View?) {
        decorView?.viewTreeObserver?.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        onGlobalLayoutListener = null
    }
}
