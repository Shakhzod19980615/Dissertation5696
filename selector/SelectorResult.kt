package ru.arzonpay.android.f_form.selector

import ru.arzonpay.android.domain.payment.FieldValue
import java.io.Serializable

class SelectorResult(
    val fieldId: String,
    val fieldValue: FieldValue
) : Serializable