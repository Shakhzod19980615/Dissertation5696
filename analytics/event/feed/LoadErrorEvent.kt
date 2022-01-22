package ru.arzonpay.android.ui.analytics.event.feed

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class LoadErrorEvent : CommonAnalyticsEvent {

    override fun key() = "feed_load_error"

    override fun params() = bundleOf()
}