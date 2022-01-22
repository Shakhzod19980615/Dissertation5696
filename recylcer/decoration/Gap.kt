package ru.arzonpay.android.ui.recylcer.decoration

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.Px
import ru.arzonpay.android.ui.recylcer.decoration.rule.DividerRule
import ru.arzonpay.android.ui.recylcer.decoration.rule.Rules.END
import ru.arzonpay.android.ui.recylcer.decoration.rule.Rules.MIDDLE

const val UNDEFINE_VIEW_HOLDER: Int = -1

/**
 * Модель для описания разделителя.
 *
 * @param color цвет разделителя.
 * @param height высота разделителя.
 * @param paddingStart отступ слева для разделителя.
 * @param paddingEnd отступ справа для разделителя.
 * @param rule правило для отрисовки разделителя.
 */
class Gap(
    @ColorInt val color: Int = Color.TRANSPARENT,
    @Px val height: Int = 0,
    @Px val paddingStart: Int = 0,
    @Px val paddingEnd: Int = 0,
    @Px val paddingTop: Int = 0,
    @DividerRule val rule: Int = MIDDLE or END
)
