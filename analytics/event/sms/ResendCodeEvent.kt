package ru.arzonpay.android.ui.analytics.event.sms

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class ResendCodeEvent : CommonAnalyticsEvent {

    override fun key() = "sms_resend_code"

    override fun params() = bundleOf()
}