package ru.arzonpay.android.ui.analytics.event.auth

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class AnonymousAuthErrorEvent : CommonAnalyticsEvent {

    override fun key() = "anonymous_auth_error"

    override fun params() = bundleOf()
}