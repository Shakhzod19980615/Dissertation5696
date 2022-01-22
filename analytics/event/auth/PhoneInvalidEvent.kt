package ru.arzonpay.android.ui.analytics.event.auth

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class PhoneInvalidEvent : CommonAnalyticsEvent {

    override fun key() = "auth_phone_invalid"

    override fun params() = bundleOf()
}