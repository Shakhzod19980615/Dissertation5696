package ru.arzonpay.android.ui.view.input

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputEditText
import ru.arzonpay.android.base.util.*
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.ui.view.extensions.getColor
import ru.arzonpay.android.ui.view.extensions.setNumericKeyboardForAllDevices
import ru.arzonpay.android.ui.view.extensions.setOnDoneAction
import ru.arzonpay.android.ui.view.extensions.setTextDistinct
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.android.utilktx.ktx.ui.view.hideSoftKeyboard
import java.math.BigDecimal
import kotlin.math.abs

/**
 * [AppCompatEditText] предназначенный для ввода суммы оплаты чего-либо.
 *
 * По-умолчанию переопределяет следующие параметры:
 * * Количество линий: 1;
 * * Тип ввода: десятичное число;
 * * IME-опции: done;
 * * Форматирование текста;
 * * Действие снятия фокуса;
 * * Действие нажатия на done, на клавиатуре.
 *
 * Для реакции на смену фокуса следует использовать [onFocusChangedAction].
 *
 * Для реакции на изменение цены следует использовать [onAmountChangedAction].
 *
 * Для реакции на нажатие done-клавиши следует использовать [onDoneClickedAction].
 *
 * Для установки визуального состояния ошибки у этого поля следует использовать [isErrorStateEnabled].
 *
 * Для предустановки либо изменения цены следует использовать [amount].
 *
 * Для изменения постфикса следует использовать [postfix].
 * */
class AmountEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    /**
     * Сумма, устанавливаемая в эту вью.
     *
     * Диффинг изменений производится автоматически.
     * */
    var amount: BigDecimal?
        get() = amountInternal
        set(value) {
            if (amountInternal == value) return
            updateAmount(amountInternal, value)
        }

    /**
     * Определяет визуальное состояние этой вью, отображающее ошибку.
     *
     * Диффинг изменений производится автоматически.
     * */
    var isErrorStateEnabled: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            updateTextColor()
        }

    /** Действие, выполняемое при нажатии на кнопку клавиатуры "Done". */
    var onDoneClickedAction: () -> Unit = { /* empty body */ }

    /** Действие, выполняемое при изменении числа в этой вью. */
    var onAmountChangedAction: (oldAmount: BigDecimal?, newAmount: BigDecimal?) -> Unit =
        { _, _ -> /* empty body */ }

    /** Действие, выполняемое при изменении фокуса EditText'а. */
    var onFocusChangedAction: (Boolean) -> Unit = { /* empty body*/ }

    private var amountInternal: BigDecimal? = null

    init {
        setSingleLine()
        inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
        imeOptions = EditorInfo.IME_ACTION_DONE
        setNumericKeyboardForAllDevices()

        addTextChangedListener(CartTextWatcher())
        setOnFocusChangeListener { _, hasFocus ->
            updateAmount()
            onFocusChangedAction(hasFocus)
        }

        setOnDoneAction {
            hideSoftKeyboard()
            clearFocus()
            onDoneClickedAction()
        }
    }

    private fun updateAmount(oldAmount: BigDecimal? = amount, newAmount: BigDecimal? = amount) {
        val oldAmountString = oldAmount?.toString() ?: EMPTY_STRING
        val newAmountString = newAmount?.toString() ?: EMPTY_STRING
        updateAmount(oldAmountString, newAmountString)
    }

    private fun updateAmount(oldAmountString: String, newAmountString: String) {
        val generatedAmountString = generateNewAmountString(oldAmountString, newAmountString)
        val generatedAmount = when {
            generatedAmountString.isNotBlank() -> BigDecimal(generatedAmountString)
            else -> null
        }
        val oldAmountInternal = amountInternal
        amountInternal = generatedAmount

        val generatedAmountFormattedString = formatAmount(generatedAmountString)
        setTextDistinct(generatedAmountFormattedString)

        val isAmountChanged = oldAmountString != newAmountString
        if (isAmountChanged) onAmountChangedAction(oldAmountInternal, amountInternal)

        updateTextColor()
    }

    private fun generateNewAmountString(oldAmount: String, newAmount: String): String {
        return run {
            val newAmountDigitsCount = newAmount.filter(Char::isDigit).length
            val oldAmountDigitsCount = oldAmount.filter(Char::isDigit).length

            val becameBlank = newAmount.none(Char::isDigit)
            if (becameBlank) return@run EMPTY_STRING // Пользователь удалил число

            val becameZero = newAmount.all { it == ZERO_CHAR }
            if (becameZero) return@run ZERO_STRING // Пользователь ввел ноль
            val wasZero = oldAmount.all { it == ZERO_CHAR }

            val newSeparatorIndex = newAmount.indexOfFirst { it.isOneOf(COMMA_CHAR, DOT_CHAR) }
            val oldSeparatorIndex = oldAmount.indexOfFirst { it.isOneOf(COMMA_CHAR, DOT_CHAR) }

            val hasSeparator = newSeparatorIndex != -1
            val hadSeparator = oldSeparatorIndex != -1

            val newIntegerPart = when {
                hasSeparator -> newAmount.substring(0 until newSeparatorIndex)
                else -> newAmount
            }
            val oldIntegerPart = when {
                hadSeparator -> oldAmount.substring(0 until oldSeparatorIndex)
                else -> oldAmount
            }

            val newDecimalPart = when {
                newSeparatorIndex.isOneOf(-1, newAmount.lastIndex) -> EMPTY_STRING
                else -> newAmount.substring(newSeparatorIndex + 1..newAmount.lastIndex)
            }
            val oldDecimalPart = when {
                oldSeparatorIndex.isOneOf(-1, oldAmount.lastIndex) -> EMPTY_STRING
                else -> oldAmount.substring(oldSeparatorIndex + 1..oldAmount.lastIndex)
            }

            val hasIntegerPart = newIntegerPart.isNotBlank()
            val hadIntegerPart = oldIntegerPart.isNotBlank()

            val isNewIntegerPartEqualsToZero = newIntegerPart.all { it == ZERO_CHAR }
            val isOldIntegerPartEqualsToZero = oldIntegerPart.all { it == ZERO_CHAR }

            val hasDecimalPart = newDecimalPart.isNotBlank()
            val hadDecimalPart = oldDecimalPart.isNotBlank()

            // При обычном вводе -- amount изменяется на один символ, переменная будет равна false.
            // Если пользователь вставит, либо вырежет текст из поля ввода -- переменная будет true.
            // В случае, когда переменная true -- будут произведены дополнительные проверки в соответствующей ветке when'а.
            val isPastedOrCutted = abs(oldAmountDigitsCount - newAmountDigitsCount) > 1
            if (isPastedOrCutted) {
                val frontZerosCount = newIntegerPart.takeWhile { it == ZERO_CHAR }.length
                val newDecimalPartFormatted = newDecimalPart.take(MAX_LEN_AFTER_SPLITTER)
                val newIntegerPartFormatted = when {
                    !hasIntegerPart -> ZERO_STRING
                    frontZerosCount != 0 -> newIntegerPart.drop(frontZerosCount)
                    else -> newIntegerPart
                }.take(MAX_LEN_BEFORE_SPLITTER)

                return@run when {
                    hasDecimalPart -> "$newIntegerPartFormatted.$newDecimalPartFormatted"
                    hasIntegerPart -> newIntegerPartFormatted
                    else -> ZERO_STRING
                }
            }

            if (!hadSeparator && hasSeparator) return@run newAmount // Пользователь добавил разделитель
            if (hadSeparator && !hasSeparator) return@run oldIntegerPart // Пользователь удалил разделитель

            if (hadSeparator && hasSeparator) { // Обработка десятичного числа
                val frontZerosCount = newIntegerPart.takeWhile { it == ZERO_CHAR }.length
                val newIntegerPartFormatted = when {
                    !hasIntegerPart || isNewIntegerPartEqualsToZero -> ZERO_STRING
                    !isNewIntegerPartEqualsToZero && frontZerosCount > 0 -> newIntegerPart.drop(
                        frontZerosCount
                    )
                    else -> newIntegerPart
                }.take(MAX_LEN_BEFORE_SPLITTER)

                val newDecimalPartFormatted = when {
                    hasDecimalPart -> newDecimalPart.take(MAX_LEN_AFTER_SPLITTER)
                    else -> EMPTY_STRING
                }
                return@run "$newIntegerPartFormatted.$newDecimalPartFormatted"
            }

            return@run when {
                wasZero -> newAmount.removePrefix(ZERO_STRING)
                newAmount.length > MAX_LEN_BEFORE_SPLITTER -> newAmount.take(MAX_LEN_BEFORE_SPLITTER)
                else -> newAmount
            }
        }.replace(COMMA_CHAR, DOT_CHAR)
    }

    private fun updateTextColor() {
        val textColor = when {
            isErrorStateEnabled -> R.attr.colorError
            amount == BigDecimal.ZERO -> android.R.attr.textColorTertiary
            else -> android.R.attr.textColorPrimary
        }.let { getColor(it) }

        setTextColor(textColor)
    }

    private fun formatAmount(amount: String): String {
        val amountBeforeSplitter = amount.takeWhile { !it.isSplitter() }
        return when (amountBeforeSplitter.length) {
            4 -> {
                val firstChunk = amount.substring(0 until 1)
                val secondChunk = amount.substring(1 until amount.length)
                "$firstChunk $secondChunk"
            }
            5 -> {
                val firstChunk = amount.substring(0 until 2)
                val secondChunk = amount.substring(2 until amount.length)
                "$firstChunk $secondChunk"
            }
            6 -> {
                val firstChunk = amount.substring(0 until 3)
                val secondChunk = amount.substring(3 until amount.length)
                "$firstChunk $secondChunk"
            }
            7 -> {
                val firstChunk = amount.substring(0 until 1)
                val secondChunk = amount.substring(1 until 4)
                val thirdChunk = amount.substring(4 until amount.length)
                "$firstChunk $secondChunk $thirdChunk"
            }
            else -> amount
        }.replace(DOT_CHAR, COMMA_CHAR)
    }

    private fun String.ensureSingleSplitter(): String {
        val isTooManySplitters = count { it.isSplitter() } > 1
        return when {
            isTooManySplitters -> {
                var hitCount = 0
                val splitterIndexToRemove = indexOfFirst { it.isSplitter() && ++hitCount > 1 }
                if (splitterIndexToRemove > 0) substring(0 until splitterIndexToRemove) else this
            }
            else -> this
        }
    }

    private fun String.filterWhitespace() = filter { !it.isWhitespace() }

    private fun Char.isSplitter() = isOneOf(COMMA_CHAR, DOT_CHAR)

    /** Читает пользовательский ввод и производит форматирование. */
    private inner class CartTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            /* empty body */
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            /* empty body */
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            removeTextChangedListener(this)
            val oldAmountString = amountInternal?.toString()?.filterWhitespace() ?: EMPTY_STRING
            val newAmountString =
                s?.toString()?.filterWhitespace()?.ensureSingleSplitter() ?: EMPTY_STRING
            updateAmount(oldAmountString, newAmountString)
            addTextChangedListener(this)
        }
    }

    private companion object {
        const val MAX_LEN_BEFORE_SPLITTER = 7
        const val MAX_LEN_AFTER_SPLITTER = 2
    }
}
