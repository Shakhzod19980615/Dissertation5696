package ru.arzonpay.android.ui.analytics.event.auth

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class ErrorEvent : CommonAnalyticsEvent {

    override fun key() = "auth_error"

    override fun params() = bundleOf()
}