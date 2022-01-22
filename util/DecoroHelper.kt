package ru.arzonpay.android.ui.util

import android.widget.EditText
import ru.surfstudio.android.utilktx.ktx.ui.view.selectionToEnd
import ru.tinkoff.decoro.FormattedTextChangeListener
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

/** Функция-расширение для [EditText], добавляющая маску для ввода номера карты. */
fun EditText.addCardNumberMask() = DecoroHelper.addCardNumberMask(this)

/** Функция-расширение, для форматирования строки с номером телефона. */
fun CharSequence.formatPhone() = DecoroHelper.formatPhone(this)

/** Функция-расширение, для форматирования строки с Узбекистанским номером телефона. */
fun CharSequence.formatUzPhone() = DecoroHelper.formatUzPhone(this)

/** Функция-расширение, для форматирования строки с номером карты. */
fun CharSequence.formatCardNumber() = DecoroHelper.formatCardNumber(this)

/** Хелпер для работы с Decoro. */
internal object DecoroHelper {

    private const val CARD_NUMBER_SLOTS = "____   ____   _____"

    private val cardNumberSlots by lazy {
        UnderscoreDigitSlotsParser().parseSlots(CARD_NUMBER_SLOTS)
    }

    /** Форматирование телефонного номера с маской по умолчанию. */
    fun formatPhone(phoneNumber: CharSequence): String {
        val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        mask.insertFront(phoneNumber)
        return mask.toString()
    }

    /** Форматирование телефонного номера с Уз маской */
    fun formatUzPhone(phoneNumber: CharSequence): String {
        val slots = UnderscoreDigitSlotsParser().parseSlots("+998 (__) ___-__-__")
        val mask = MaskImpl.createTerminated(slots)
        mask.insertFront(phoneNumber)
        return mask.toString()
    }

    /** Добавление [EditText] маски для корректного ввода и отображения номера карты. */
    fun addCardNumberMask(editText: EditText) {
        val mask = MaskImpl.createTerminated(cardNumberSlots)

        val formatWatcher = MaskFormatWatcher(mask)
        formatWatcher.setCallback(createFormatWatcherCallback(editText))
        formatWatcher.installOn(editText)
    }

    /** Форматирование номера карты по предустановленному паттерну. */
    fun formatCardNumber(cardNumber: CharSequence): String {
        val mask = MaskImpl.createTerminated(cardNumberSlots)
        mask.insertFront(cardNumber)
        return mask.toString()
    }

    private fun createFormatWatcherCallback(editText: EditText): FormattedTextChangeListener =
        object : FormattedTextChangeListener {
            override fun onTextFormatted(formatter: FormatWatcher?, newFormattedText: String?) {
                editText.selectionToEnd()
            }

            override fun beforeFormatting(oldValue: String?, newValue: String?): Boolean {
                return false
            }
        }
}
