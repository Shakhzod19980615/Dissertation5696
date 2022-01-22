package ru.arzonpay.android.ui.analytics.event.form

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class FormSendSuccessEvent(
    private val id: String
) : CommonAnalyticsEvent {

    override fun key() = "form_success"

    override fun params() = bundleOf(
        "payment_id" to id
    )
}