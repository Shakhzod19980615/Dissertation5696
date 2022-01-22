package ru.arzonpay.android.ui.dialog.base.simple

import java.io.Serializable

/**
 * Результат работы простого диалога.
 *
 * @property POSITIVE нажата кнопка ОК
 * @property NEGATIVE нажата кнопка Отмены
 * @property DISMISS диалог отменен (по беку или по тапу за границу диалога)
 */
enum class SimpleResult : Serializable {
    POSITIVE,
    NEGATIVE,
    DISMISS
}
