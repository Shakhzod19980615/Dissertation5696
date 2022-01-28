package ru.arzonpay.android.f_details.data

import ru.arzonpay.android.domain.Currency
import ru.arzonpay.android.domain.payment.Status as PaymentStatus

internal sealed class FieldUi {

    data class Info(
        val title: String,
        val value: String
    ) : FieldUi()

    data class Amount(
        val title: String,
        val value: String,
        val currency: Currency
    ) : FieldUi()

    data class Status(
        val status: PaymentStatus
    ) : FieldUi(), NoBgField
}