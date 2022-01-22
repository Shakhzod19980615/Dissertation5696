package ru.arzonpay.android.ui.analytics.event.feed

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class PaymentEvent(
    private val id: String
) : CommonAnalyticsEvent {

    override fun key() = "payment"

    override fun params() = bundleOf(
        "id" to id
    )
}