package ru.arzonpay.android.ui.view.extensions

import android.content.res.ColorStateList
import android.text.TextUtils
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import ru.surfstudio.android.utilktx.ktx.ui.view.setDrawableColor
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Свойство производит самостоятельный Diff'ing текста и применяет его только тогда,
 * когда установленный в [TextView] текст и новый текст не совпадают.
 *
 * Таким образом мы уменьшаем количество перерисовок и инвалидаций [TextView],
 * а также избегаем потенциального бесконечного loop'a установки текста при неправильном использовании RxJava
 * (забыли навесить оператор distinctUntilChanged на textChangesObservable).
 * */
var TextView.distinctText: CharSequence
    get() = text
    set(value) {
        setTextDistinct(value)
    }

/**
 * Метод производит самостоятельный Diff'ing текста и применяет его только тогда,
 * когда установленный в [TextView] текст и [newText] не совпадают.
 *
 * Таким образом мы уменьшаем количество перерисовок и инвалидаций [TextView],
 * а также избегаем потенциального бесконечного loop'a установки текста при неправильном использовании RxJava
 * (забыли навесить оператор distinctUntilChanged на textChangesObservable).
 *
 * @return был ли изменен текст [TextView].
 * */
fun TextView.setTextDistinct(newText: CharSequence): Boolean {
    if (TextUtils.equals(newText, text)) return false
    text = newText
    return true
}

/**
 * @return поток изменений текста, конвертированный в [String]
 */
fun TextView.textChangesString(): Observable<String> {
    return textChanges().map(CharSequence::toString)
}

/**
 * @return поток изменений текста, конвертированный в [String] с пропуском начального значения
 */
fun TextView.textChangesStringSkipFirst(): Observable<String> {
    return textChanges().skipInitialValue().map(CharSequence::toString)
}

/** Устанавливает цвет у `compoundDrawables` специфичным для используемой версии OS алгоритмом. */
fun TextView.setDrawableTintCompat(color: Int) {
    when {
        SdkUtils.isAtLeastNougat() -> compoundDrawableTintList = ColorStateList.valueOf(color)
        else -> setDrawableColor(color)
    }
}