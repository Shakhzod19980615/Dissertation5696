package ru.arzonpay.android.ui.analytics.event.auth

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class AuthorizedEvent : CommonAnalyticsEvent {

    override fun key() = "auth_success"

    override fun params() = bundleOf()
}