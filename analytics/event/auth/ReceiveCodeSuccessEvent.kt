package ru.arzonpay.android.ui.analytics.event.auth

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class ReceiveCodeSuccessEvent : CommonAnalyticsEvent {

    override fun key() = "auth_receive_code_success"

    override fun params() = bundleOf()
}