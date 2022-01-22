package ru.arzonpay.android.ui.analytics.event.form

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class FormFilledErrorEvent(
    private val id: String,
    private val fields: List<String>
) : CommonAnalyticsEvent {

    override fun key() = "form_filled_error"

    override fun params() = bundleOf(
        "payment_id" to id,
        "which" to fields.joinToString(":")
    )
}