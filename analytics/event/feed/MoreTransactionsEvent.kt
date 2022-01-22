package ru.arzonpay.android.ui.analytics.event.feed

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class MoreTransactionsEvent : CommonAnalyticsEvent {

    override fun key() = "feed_more_transactions"

    override fun params() = bundleOf()
}