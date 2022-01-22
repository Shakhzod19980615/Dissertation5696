package ru.arzonpay.android.ui.analytics.event.feed

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class CardTransferFilledErrorEvent(
    private val isAmountError: Boolean,
    private val isCardError: Boolean
) : CommonAnalyticsEvent {

    override fun key() = "feed_transfer_card_filled_error"

    override fun params() = bundleOf(
        "which" to CardTransferErrorType.fromErrors(isAmountError, isCardError).value
    )
}