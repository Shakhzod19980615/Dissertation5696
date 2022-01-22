package ru.arzonpay.android.ui.analytics.event.confirm

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class ConfirmationErrorEvent : CommonAnalyticsEvent {

    override fun key() = "confirmation_error"

    override fun params() = bundleOf()
}