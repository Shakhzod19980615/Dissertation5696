package ru.arzonpay.android.ui.view.extensions

import android.content.res.Configuration
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import ru.arzonpay.android.ui.util.DeviceSpecificUtil
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Расширение для установки inputType без потери шрифта
 */
fun EditText.changeInputType(type: Int) {
    if (type != EditorInfo.TYPE_NULL) {
        val currentTypeface = typeface
        inputType = type
        typeface = currentTypeface // смена inputType влечет сброс шрифта у поля ввода
    }
}

/**
 * Обновляет текст в поле ввода только если он изменился, сохраняя позицию курсора
 */
var EditText.distinctText: CharSequence
    get() = this.text.toString()
    set(value) {
        if (TextUtils.equals(value, this.text.toString())) return
        setTextDistinct(value)
    }

/**
 * Метод производит самостоятельный Diff'ing текста и применяет его только тогда,
 * когда установленный в [EditText] текст и [newText] не совпадают.
 *
 * Также не меняет позицию выделения в [EditText].
 *
 * Таким образом мы уменьшаем количество перерисовок и инвалидаций [EditText],
 * а также избегаем потенциального бесконечного loop'a установки текста при неправильном использовании RxJava
 * (забыли навесить оператор distinctUntilChanged на textChangesObservable).
 *
 * @return был ли изменен текст [EditText].
 * */
fun EditText.setTextDistinct(newText: CharSequence): Boolean {
    val isCursorPosGreaterThanTextLen = selectionStart > newText.length
    val isCursorAtTheEnd = selectionStart == length()
    tag = false
    val isChanged = (this as TextView).setTextDistinct(newText)
    val isFormatterWork = newText.length < text.length
    val newCursorPos = when {
        isFormatterWork -> text.length
        isCursorPosGreaterThanTextLen || isCursorAtTheEnd -> newText.length
        else -> selectionStart
    }
    if (isChanged) setSelection(newCursorPos)
    tag = true
    return isChanged
}

/**
 * Расширение для установки ограничения по количеству символов в EditText
 */
fun EditText.setLengthLimit(limit: Int) {
    val lengthFilter: InputFilter = InputFilter.LengthFilter(limit)
    val filtersWithoutLengthFilter = filters.filter { it !is InputFilter.LengthFilter }
    val newFilters = (filtersWithoutLengthFilter + lengthFilter).toTypedArray()
    filters = newFilters
}

/**
 * Расширение для добавляения фильтра
 */
fun EditText.addInputFilter(filter: InputFilter) {
    filters = arrayOf(*this.filters, filter)
}

/**
 * Расширение для EditText, добавляет listener на изменение текста и меняет его согласно коллбеку
 *
 * @param onTextChanged - коллбек, вызываемый каждый раз при изменении текста
 */
fun EditText.setOnTextChanged(onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (tag != false) {
                removeTextChangedListener(this)
                onTextChanged.invoke(s?.toString() ?: EMPTY_STRING)
                addTextChangedListener(this)
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    })
}

/**
 * Расширение, позволяющее отобразить цифровую клавиатуру без возможности перехода на буквы.
 *
 * На некоторых девайсах при установке inputType="number" для [EditText]
 * открывается цифровая клавиатура с посторонними символами и возможностью переключения на символьную.
 */
fun EditText.setNumericKeyboardForAllDevices() {
    if (DeviceSpecificUtil.isDeviceWithCustomDigitalKeyboard) {
        setRawInputType(Configuration.KEYBOARD_12KEY)
    }
}

/**
 * Расширение для [EditText].
 *
 * Устанавливает `OnEditorActionListener`, который реагирует на действие `imeDone` выполняя код из `block`.
 *
 * **Очищает все ранее установленные `OnEditorActionListener`'ы!**
 */
fun EditText.setOnDoneAction(block: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        return@setOnEditorActionListener when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                block()
                true
            }
            else -> false
        }
    }
}

var TextInputLayout.errorText: CharSequence?
    get() = error
    set(value) {
        if (value != null) {
            isErrorEnabled = true
        }
        error = value
        if (value == null) {
            isErrorEnabled = false
        }
    }