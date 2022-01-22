package ru.arzonpay.android.ui.analytics.event.feed

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class CardTransferSuccessEvent : CommonAnalyticsEvent {

    override fun key() = "feed_transfer_to_card_success"

    override fun params() = bundleOf()
}