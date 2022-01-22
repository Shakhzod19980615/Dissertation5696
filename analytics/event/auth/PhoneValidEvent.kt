package ru.arzonpay.android.ui.analytics.event.auth

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class PhoneValidEvent : CommonAnalyticsEvent {

    override fun key() = "auth_phone_valid"

    override fun params() = bundleOf()
}