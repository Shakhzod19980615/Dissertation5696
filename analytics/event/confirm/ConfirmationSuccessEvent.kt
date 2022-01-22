package ru.arzonpay.android.ui.analytics.event.confirm

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class ConfirmationSuccessEvent : CommonAnalyticsEvent {

    override fun key() = "confirmation_success"

    override fun params() = bundleOf()
}