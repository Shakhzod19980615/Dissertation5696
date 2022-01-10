package ru.arzonpay.android.base.util

/**
 * Функция-расширение для удобного сравнения одного значения с множеством других.
 */
fun <T : Any> T.isOneOf(vararg values: T): Boolean = values.any(::equals)