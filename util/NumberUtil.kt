package ru.arzonpay.android.ui.util

import ru.arzonpay.android.base.util.*
import ru.surfstudio.android.utilktx.ktx.text.SPACE
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * Символы для форматирования чисел.
 */
private val defaultFormatSymbols = DecimalFormatSymbols().apply {
    decimalSeparator = COMMA_CHAR
    groupingSeparator = SPACE_CHAR
}

/**
 * Форматтер десятичных чисел.
 */
val defaultNumericFormat = DecimalFormat(NUMERIC_PATTERN, defaultFormatSymbols).apply {
    isGroupingUsed = true
    groupingSize = 3
}

/**
 * Форматирование [BigDecimal] с использованием [defaultNumericFormat].
 */
fun getFormattedSum(sum: BigDecimal): CharSequence {
    val formattedSum = defaultNumericFormat.format(sum)
    // Если после запятой только одно число -- добавляем к нему еще ноль,
    // чтобы у нас получились сотые с дробной части.
    val isDecimal = formattedSum.contains(COMMA_CHAR)
    return when {
        isDecimal -> {
            val isFullPattern = formattedSum.substringAfter(COMMA_CHAR).length == 2
            if (isFullPattern) formattedSum else "${formattedSum}0"
        }
        else -> formattedSum
    }
}

/**
 * Форматированние числового значения в последовательность суммы.
 */
fun getFormattedSumWithRub(sum: BigDecimal): CharSequence {
    val formattedString = "${getFormattedSum(sum)} $RUB_SYMBOL"
    return formattedString.replace(SPACE, NO_BREAK_SPACE)
}

/**
 * Форматированние числового значения в последовательность суммы.
 */
fun getFormattedSumWithSom(sum: BigDecimal): CharSequence {
    val formattedString = "${getFormattedSum(sum)} $SUM_SYMBOL"
    return formattedString.replace(SPACE, NO_BREAK_SPACE)
}

/**
 * Возвращает число написанное в начале строки
 * пр. 360 дней -> 360
 */
fun String.getFirstDigits(): String {
    return this.takeWhile { it.isDigit() }
}

/**
 * Возвращает десятичное число (с дробной частью, если имеется) написанное в начале строки.
 *
 * Например: "360.15 рублей" -> "360.15".
 */
fun String.getFirstDigitsDecimal(fallback: String = ZERO_STRING): String {
    val result = takeWhile { it.isDigit() || it.isOneOf(COMMA_CHAR, DOT_CHAR) }
        .replace(COMMA_CHAR, DOT_CHAR)

    return when {
        result.isNotBlank() -> result
        else -> fallback
    }
}
