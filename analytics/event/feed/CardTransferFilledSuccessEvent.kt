package ru.arzonpay.android.ui.analytics.event.feed

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class CardTransferFilledSuccessEvent : CommonAnalyticsEvent {

    override fun key() = "feed_transfer_card_filled_success"

    override fun params() = bundleOf()
}