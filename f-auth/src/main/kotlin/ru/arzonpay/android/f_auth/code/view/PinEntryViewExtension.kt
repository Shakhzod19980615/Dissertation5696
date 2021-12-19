package ru.arzonpay.android.f_auth.code.view

import androidx.annotation.Px
import ru.arzonpay.android.ui.view.extensions.displayMetrics
import ru.surfstudio.android.utilktx.ktx.ui.view.setLeftMargin
import ru.surfstudio.android.utilktx.ktx.ui.view.setRightMargin
import kotlin.math.roundToInt

const val DEFAULT_PIN_ENTRY_VIEW_DIGIT_NUMBER = 4
private const val DEFAULT_GUTTER_WIDTH_PERCENT = 0.04
private const val DEFAULT_DIGIT_HEIGHT_PERCENT = 0.17
private const val SPACING_WIDTH_TO_DIGIT_WIDTH_RATIO = 2.2
private const val SPACING_WIDTH_COEF = 3.37

/**
 * Масштабирование [PinEntryView] по [parentWidth]
 */
fun PinEntryView.fitParent(@Px parentWidth: Int, @Px horizontalMargin: Int? = null) {
    val gutterWidth = when {
        horizontalMargin != null -> horizontalMargin
        else -> (parentWidth * DEFAULT_GUTTER_WIDTH_PERCENT).roundToInt()
    }

    val parentWidthWithoutGutter = parentWidth - gutterWidth

    val spacingWidth =
        parentWidthWithoutGutter / (SPACING_WIDTH_COEF * digitNumber - 1).roundToInt()
    val digitWidth = (spacingWidth * SPACING_WIDTH_TO_DIGIT_WIDTH_RATIO).roundToInt()
    val digitHeight = (parentWidth * DEFAULT_DIGIT_HEIGHT_PERCENT).roundToInt()

    setDigitWidth(digitWidth)
    setDigitHeight(digitHeight)
    setDigitSpacing(spacingWidth)
    setLeftMargin(gutterWidth)
    setRightMargin(gutterWidth)

    resetView()
}

/**
 * Масштабирование [PinEntryView] по ширине экрана.
 *
 * **Внимание! Не использовать в ландшафтной ориентации!**
 */
fun PinEntryView.fitScreenWidth(@Px horizontalMargin: Int? = null) {
    fitParent(displayMetrics.widthPixels, horizontalMargin)
}
