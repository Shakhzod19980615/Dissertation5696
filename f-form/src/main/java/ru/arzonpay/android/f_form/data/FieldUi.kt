package ru.arzonpay.android.f_form.data

import ru.arzonpay.android.domain.Currency
import ru.arzonpay.android.domain.payment.FieldValue
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import java.math.BigDecimal

sealed class FieldUi {

    abstract val fieldName: String
    abstract val fieldValue: String
    abstract val isValid: Boolean

    data class Card(
        val name: String,
        val hint: String,
        val value: String = EMPTY_STRING,
        val extractedValue: String = EMPTY_STRING,
        val helperText: String? = null,
        val errorText: String? = null,
    ) : FieldUi() {

        override val fieldName = name
        override val fieldValue = value

        override val isValid: Boolean
            get() = errorText == null
    }

    data class Digits(
        val name: String,
        val hint: String,
        val value: String = EMPTY_STRING,
        val helperText: String? = null,
        val errorText: String? = null,
    ) : FieldUi() {

        override val fieldName = name
        override val fieldValue = value

        override val isValid: Boolean
            get() = errorText == null
    }

    data class Multiline(
        val name: String,
        val hint: String,
        val value: String = EMPTY_STRING,
        val helperText: String? = null,
        val errorText: String? = null,
    ) : FieldUi() {

        override val fieldName = name
        override val fieldValue = value

        override val isValid: Boolean
            get() = errorText == null
    }

    data class Money(
        val name: String,
        val amount: BigDecimal? = null,
        val currency: Currency = Currency.SUM,
        val helperText: String? = null,
        val errorText: String? = null,
    ) : FieldUi() {

        override val fieldName = name
        override val fieldValue = amount?.toString() ?: EMPTY_STRING

        override val isValid: Boolean
            get() = errorText == null
    }

    data class Phone(
        val name: String,
        val value: String = EMPTY_STRING,
        val extractedValue: String = EMPTY_STRING,
        val helperText: String? = null,
        val errorText: String? = null,
    ) : FieldUi() {

        override val fieldName = name
        override val fieldValue = extractedValue

        override val isValid: Boolean
            get() = errorText == null
    }

    data class Selector(
        val name: String,
        val hint: String,
        val id: String,
        val value: FieldValue? = null,
        val parent: String? = null,
        val errorText: String? = null
    ) : FieldUi() {

        override val fieldName = name
        override val fieldValue = value?.value ?: EMPTY_STRING

        override val isValid: Boolean
            get() = errorText == null
    }

    data class Text(
        val name: String,
        val hint: String,
        val value: String = EMPTY_STRING,
        val helperText: String? = null,
        val errorText: String? = null
    ) : FieldUi() {

        override val fieldName = name
        override val fieldValue = value

        override val isValid: Boolean
            get() = errorText == null
    }
}