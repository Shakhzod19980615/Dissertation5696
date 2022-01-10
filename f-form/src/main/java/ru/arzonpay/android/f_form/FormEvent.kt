package ru.arzonpay.android.f_form

import ru.arzonpay.android.domain.Currency
import ru.arzonpay.android.domain.payment.Field
import ru.arzonpay.android.domain.payment.FieldValue
import ru.arzonpay.android.domain.payment.Transaction
import ru.arzonpay.android.f_form.data.FieldUi
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsComposition
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsEvent
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.ui.state.LifecycleStage
import java.math.BigDecimal

internal sealed class FormEvent : Event {

    data class Navigation(override var event: NavCommandsEvent = NavCommandsEvent()) :
        NavCommandsComposition, FormEvent()

    data class Lifecycle(override var stage: LifecycleStage) : FormEvent(), LifecycleEvent

    sealed class Input : FormEvent() {
        object BackClicked : Input()
        object PayClicked : Input()

        data class SelectorClicked(
            val field: FieldUi.Selector
        ) : Input()
    }

    object ProcessPayment : FormEvent()

    sealed class Fields : FormEvent() {
        data class CardEdited(
            val field: FieldUi.Card,
            val formatted: String,
            val extracted: String
        ) : Fields()

        data class DigitEdited(
            val field: FieldUi.Digits,
            val value: String
        ) : Fields()

        data class MoneyEdited(
            val field: FieldUi.Money,
            val amount: BigDecimal?,
            val currency: Currency
        ) : Fields()

        data class AmountCalculated(
            val field: FieldUi.Money,
            val amount: BigDecimal?,
            val currency: Currency,
            val convertedAmount: BigDecimal?,
            val convertedCurrency: Currency
        ) : Fields()

        data class MultilineEdited(
            val field: FieldUi.Multiline,
            val value: String
        ) : Fields()

        data class PhoneEdited(
            val field: FieldUi.Phone,
            val formatted: String,
            val extracted: String
        ) : Fields()

        data class TextEdited(
            val field: FieldUi.Text,
            val value: String
        ) : Fields()
    }

    data class ConfigLoaded(
        val rate: BigDecimal,
        val minUzs: BigDecimal,
        val maxUzs: BigDecimal
    ) : FormEvent()

    data class FieldValueSelected(
        val fieldId: String,
        val value: FieldValue
    ) : FormEvent()

    data class FormRequest(
        override val request: Request<List<Field>>,
    ) : FormEvent(), RequestEvent<List<Field>>

    data class SendFormRequest(
        override val request: Request<Transaction>
    ) : FormEvent(), RequestEvent<Transaction>

    data class GetFieldValues(
        val fieldId: String,
        override val request: Request<List<FieldValue>>
    ) : FormEvent(), RequestEvent<List<FieldValue>>
}
