package ru.arzonpay.android.ui.analytics.event.form

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class LoadErrorEvent(
    private val id: String
) : CommonAnalyticsEvent {

    override fun key() = "form_load_error"

    override fun params() = bundleOf(
        "payment_id" to id
    )
}