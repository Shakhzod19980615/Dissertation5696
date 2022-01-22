package ru.arzonpay.android.ui.analytics.event.sms

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class CodeErrorEvent : CommonAnalyticsEvent {

    override fun key() = "sms_code_error"

    override fun params() = bundleOf()
}